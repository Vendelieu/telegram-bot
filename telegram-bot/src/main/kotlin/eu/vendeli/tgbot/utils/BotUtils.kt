@file:Suppress("MatchingDeclarationName", "TooManyFunctions")

package eu.vendeli.tgbot.utils

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import eu.vendeli.tgbot.api.botactions.getUpdates
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.core.TgUpdateHandler.Companion.logger
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ChainLink
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.http.escapeIfNeeded
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonUnquotedLiteral
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.serializer

internal suspend inline fun TgUpdateHandler.coHandle(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    crossinline block: suspend CoroutineScope.() -> Unit,
) = (handlerScope + Job(handlerScope.coroutineContext[Job])).launch(dispatcher) { block() }

internal suspend inline fun TgUpdateHandler.checkIsLimited(
    limits: RateLimits,
    telegramId: Long? = null,
    actionId: String? = null,
): Boolean = bot.config.rateLimiter.run {
    if (telegramId == null || limits.period == 0L && limits.rate == 0L) return false

    logger.debug { "Checking the request for exceeding the limits${if (actionId != null) " for $actionId}" else ""}." }
    if (mechanism.isLimited(limits, telegramId, actionId)) {
        val loggingTail = if (actionId != null) " for $actionId}" else ""
        logger.info { "User #$telegramId has exceeded the request limit$loggingTail." }
        exceededAction.invoke(telegramId, bot)
        return true
    }
    return false
}

internal inline val <K, V : Any> Map<K, V>.logString: String
    get() = takeIf { isNotEmpty() }?.entries?.joinToString(",\n") {
        "${it.key} - " + if (it.value is Pair<*, *>) {
            (it.value as Pair<*, *>).second
        } else {
            it.value
        }.toString()
    } ?: "None"

@OptIn(InternalSerializationApi::class)
@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type : Any> TgAction<Type>.getReturnType(): KSerializer<Type> = Type::class.serializer()

@Suppress("UnusedReceiverParameter")
@OptIn(InternalSerializationApi::class)
@JvmName("listReturnType")
internal inline fun <reified Type : MultipleResponse> TgAction<List<Type>>.getReturnType(): KSerializer<List<Type>> =
    ListSerializer(Type::class.serializer())

internal fun Map<*, *>.toJsonElement(): JsonElement = buildJsonObject {
    val map = mutableMapOf<String, JsonElement>()
    this@toJsonElement.forEach { (key, value) ->
        key as String
        map[key] = value.toJsonElement()
    }
}

internal fun Collection<*>.toJsonElement(): JsonElement = JsonArray(map { it.toJsonElement() })
internal fun Array<*>.toJsonElement(): JsonElement = JsonArray(map { it.toJsonElement() })

@OptIn(ExperimentalSerializationApi::class)
internal fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> JsonNull
    is JsonElement -> this
    is Map<*, *> -> toJsonElement()
    is Collection<*> -> toJsonElement()
    is ByteArray -> this.toList().toJsonElement()
    is Array<*> -> toJsonElement()
    is Boolean -> JsonPrimitive(this)
    is Number -> JsonPrimitive(this)
    is String -> JsonPrimitive(this)
    else -> serde.encodeToJsonElement(DynamicLookupSerializer, this)
}

@OptIn(ExperimentalSerializationApi::class)
@Suppress("NOTHING_TO_INLINE")
internal inline fun <R> TgAction<R>.handleImplicitFile(input: ImplicitFile, fieldName: String) {
    if (input is ImplicitFile.Str) {
        parameters[fieldName] = JsonPrimitive(input.file)
    } else if (input is ImplicitFile.InpFile) {
        multipartData += input.file.toPartData(input.file.fileName)
        parameters[fieldName] = JsonUnquotedLiteral("attach://${input.file.fileName}")
    }
}

internal fun InputFile.toPartData(name: String) = PartData.BinaryItem(
    { ByteReadPacket(data) },
    {},
    Headers.build {
        append(HttpHeaders.ContentDisposition, "form-data; name=${name.escapeIfNeeded()}")
        append(HttpHeaders.ContentDisposition, "filename=$fileName")
        append(HttpHeaders.ContentType, contentType)
        append(HttpHeaders.ContentLength, data.size.toString())
    },
)

internal var mu.KLogger.level: Level
    get() = (underlyingLogger as Logger).level
    set(value) {
        (underlyingLogger as Logger).level = value
    }

internal val GET_UPDATES_ACTION = getUpdates()
internal val DEFAULT_COMMAND_SCOPE = setOf(UpdateType.MESSAGE)

internal suspend inline fun <T> asyncAction(crossinline block: suspend () -> T): Deferred<T> = coroutineScope {
    async { block() }
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun String.asClass(): Class<*>? = kotlin.runCatching { Class.forName(this) }.getOrNull()

@Suppress("NOTHING_TO_INLINE")
internal inline fun Class<*>?.getActions(postFix: String? = null) =
    this?.getMethod("get\$ACTIVITIES".let { if (postFix != null) it + postFix else it })?.invoke(null) as? List<*>

@Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")
internal inline fun <T> Any?.cast() = this as T

fun <T : ChainLink> InputListener.setChain(user: User, firstLink: T) = set(user, firstLink::class.qualifiedName!!)
