@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.utils

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import eu.vendeli.tgbot.core.TelegramUpdateHandler
import eu.vendeli.tgbot.core.TelegramUpdateHandler.Companion.logger
import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.Activity
import eu.vendeli.tgbot.types.internal.CommandScope
import eu.vendeli.tgbot.types.internal.Invocation
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.StructuredRequest
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.reflect.Method
import java.lang.reflect.Modifier.isStatic
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

/**
 * Creates new coroutine context from parent one and adds supervisor job.
 *
 * @param parentContext Context that will be merged with the created one.
 */
internal class NewCoroutineContext(parentContext: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext =
        parentContext + SupervisorJob(parentContext[Job]) + CoroutineName("TgBot")
}

@Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
internal fun TelegramUpdateHandler.parseCommand(
    text: String,
): StructuredRequest = with(bot.config.commandParsing) {
    var state = ParserState.READING_COMMAND
    var command = ""
    val params = mutableMapOf<String, String>()

    var paramNameBuffer = ""
    var paramValBuffer = ""

    text.forEach { i ->
        when (state) {
            ParserState.READING_COMMAND -> {
                if (i == commandDelimiter || (restrictSpacesInCommands && i == ' ')) {
                    state = ParserState.READING_PARAM_NAME
                } else command += i
            }

            ParserState.READING_PARAM_NAME -> {
                when (i) {
                    parameterValueDelimiter -> {
                        state = ParserState.READING_PARAM_VALUE
                    }

                    parametersDelimiter -> {
                        params["param_${params.size + 1}"] = paramNameBuffer
                        paramNameBuffer = ""
                    }

                    else -> paramNameBuffer += i
                }
            }

            ParserState.READING_PARAM_VALUE -> {
                if (i == parametersDelimiter) {
                    params[paramNameBuffer] = paramValBuffer
                    paramNameBuffer = ""
                    paramValBuffer = ""
                    state = ParserState.READING_PARAM_NAME
                } else paramValBuffer += i
            }
        }
    }
    if (state == ParserState.READING_PARAM_VALUE) {
        params[paramNameBuffer] = paramValBuffer
    } else if (state == ParserState.READING_PARAM_NAME) {
        params["param_${params.size + 1}"] = paramNameBuffer
    }

    if (params.isEmpty() && command.startsWith("/start ")) {
        params += "deepLink" to command.substringAfter("/start ")
        command = "/start"
    }

    return StructuredRequest(command = command, params = params)
}

@Suppress("SpreadOperator")
internal suspend inline fun Method.handleInvocation(
    clazz: Class<*>,
    classManager: ClassManager,
    parameters: Array<Any?>,
    isSuspend: Boolean = false,
): Any? {
    val obj = when {
        isStatic(modifiers) -> null
        clazz.kotlin.objectInstance != null -> clazz.kotlin.objectInstance
        else -> classManager.getInstance(clazz)
    }

    return if (isSuspend) suspendCoroutineUninterceptedOrReturn { cont ->
        invoke(obj, *parameters, cont)
    } else invoke(obj, *parameters)
}

internal suspend inline fun TelegramUpdateHandler.checkIsLimited(
    limits: RateLimits,
    telegramId: Long?,
    actionId: String? = null,
): Boolean {
    if (limits.period == 0L && limits.rate == 0L || telegramId == null) return false

    logger.debug { "Checking the request for exceeding the limits${if (actionId != null) " for $actionId}" else ""}." }
    if (rateLimiter.isLimited(limits, telegramId, actionId)) {
        val loggingTail = if (actionId != null) " for $actionId}" else ""
        logger.info { "User #$telegramId has exceeded the request limit$loggingTail." }
        rateLimiter.exceededLimitResponse(telegramId, bot)
        return true
    }
    return false
}

/**
 * Function for mapping text with a specific command or input.
 *
 * @param text
 * @param command true to search in commands or false to search among inputs. Default - true.
 * @return [Activity] if actions was found or null.
 */
internal fun TelegramUpdateHandler.findAction(
    text: String,
    command: Boolean = true,
    updateType: UpdateType,
): Activity? {
    val message = parseCommand(text)
    val invocation = if (command) actions?.commands else {
        actions?.inputs
    }?.get(message.command)

    if (invocation != null && command && updateType.scope !in invocation.scope)
        return null

    if (command && invocation == null) actions?.regexCommands?.entries?.firstOrNull {
        it.key.matchEntire(text) != null
    }?.also {
        return it.value.toActivity(message)
    }

    return invocation?.toActivity(message)
}

internal fun Invocation.toActivity(req: StructuredRequest) = Activity(
    id = req.command,
    invocation = this,
    parameters = req.params,
    rateLimits = rateLimits,
)

@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type : MultipleResponse> SimpleAction<List<Type>>.getInnerType(): Class<Type> =
    Type::class.java

@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type : MultipleResponse> Action<List<Type>>.getInnerType(): Class<Type> =
    Type::class.java

@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type> TgAction<Type>.getReturnType(): Class<Type> = Type::class.java

internal var mu.KLogger.level: Level
    get() = (underlyingLogger as Logger).level
    set(value) {
        (underlyingLogger as Logger).level = value
    }

private enum class ParserState {
    READING_COMMAND,
    READING_PARAM_NAME,
    READING_PARAM_VALUE,
}

internal val DEFAULT_COMMAND_SCOPE = setOf(CommandScope.MESSAGE, CommandScope.CALLBACK)
internal val PARAMETERS_MAP_TYPEREF = jacksonTypeRef<Map<String, Any?>>()
internal val RESPONSE_UPDATES_LIST_TYPEREF = jacksonTypeRef<Response<List<Update>>>()

internal suspend inline fun <T> asyncAction(crossinline block: suspend () -> T): Deferred<T> = coroutineScope {
    async { block() }
}
