@file:Suppress(
    "NOTHING_TO_INLINE",
    "ObjectPropertyName",
    "UNUSED_ANONYMOUS_PARAMETER",
    "UnnecessaryVariable",
    "TopLevelPropertyNaming",
    "UNNECESSARY_SAFE_CALL",
    "RedundantNullableReturnType",
    "KotlinConstantConditions",
    "USELESS_ELVIS",
    "ktlint:standard:backing-property-naming",
)
@file:OptIn(InternalApi::class)

package eu.vendeli.fixtures

import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.types.internal.CommonMatcher
import eu.vendeli.tgbot.types.internal.InvocationMeta
import eu.vendeli.tgbot.types.internal.StoredState
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.Invocable
import eu.vendeli.tgbot.utils.InvocationLambda

private inline fun suspendCall(noinline block: InvocationLambda): InvocationLambda = block

private val zeroRateLimits: RateLimits = RateLimits(0, 0)

private val __TG_COMMANDS0: Map<Pair<String, UpdateType>, Invocable> = mapOf(
    ("test2" to UpdateType.MESSAGE) to (
        suspendCall { classManager, update, user, bot, parameters ->
            if (user != null && bot.update.userClassSteps[user.id] != "eu.vendeli.fixtures")
                eu.vendeli.fixtures.____clearClassData(user.id)
            ::testMethod.invoke()
        }
            to InvocationMeta(
                "eu.vendeli.fixtures",
                "testMethod",
                zeroRateLimits,
                eu.vendeli.tgbot.implementations.DefaultGuard::class,
            )
    ),
    ("test" to UpdateType.MESSAGE) to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class) as
                eu.vendeli.fixtures.TgAnnotationsModel
            val param0 = bot
            if (user != null &&
                bot.update.userClassSteps[user.id] !=
                "eu.vendeli.fixtures.TgAnnotationsModel"
            ) eu.vendeli.fixtures.____clearClassData(user.id)
            eu.vendeli.fixtures.TgAnnotationsModel::test.invoke(
                inst,
                param0,
            )
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.TgAnnotationsModel",
                "test",
                zeroRateLimits,
                eu.vendeli.tgbot.implementations.DefaultGuard::class,
            )
    ),
    ("STOP" to UpdateType.MESSAGE) to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class) as
                eu.vendeli.fixtures.TgAnnotationsModel
            val param0 = bot
            if (user != null &&
                bot.update.userClassSteps[user.id] !=
                "eu.vendeli.fixtures.TgAnnotationsModel"
            ) eu.vendeli.fixtures.____clearClassData(user.id)
            eu.vendeli.fixtures.TgAnnotationsModel::stopHandling.invoke(
                inst,
                param0,
            )
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.TgAnnotationsModel",
                "stopHandling",
                zeroRateLimits,
                eu.vendeli.tgbot.implementations.DefaultGuard::class,
            )
    ),
    ("test3" to UpdateType.MESSAGE) to (
        suspendCall { classManager, update, user, bot, parameters ->
            if (user != null && bot.update.userClassSteps[user.id] != "eu.vendeli.fixtures.TestObj")
                eu.vendeli.fixtures.____clearClassData(user.id)
            eu.vendeli.fixtures.TestObj::test.invoke()
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.TestObj",
                "test",
                zeroRateLimits,
                eu.vendeli.tgbot.implementations.DefaultGuard::class,
            )
    ),
)

private val __TG_INPUTS0: Map<String, Invocable> = mapOf(
    "testInp2" to (
        suspendCall { classManager, update, user, bot, parameters ->
            if (user != null && bot.update.userClassSteps[user.id] != "eu.vendeli.fixtures")
                eu.vendeli.fixtures.____clearClassData(user.id)
            ::testMethod2.invoke()
        }
            to InvocationMeta(
                "eu.vendeli.fixtures",
                "testMethod2",
                zeroRateLimits,
                eu.vendeli.tgbot.implementations.DefaultGuard::class,
            )
    ),
    "testInp" to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class) as
                eu.vendeli.fixtures.TgAnnotationsModel
            if (user != null &&
                bot.update.userClassSteps[user.id] !=
                "eu.vendeli.fixtures.TgAnnotationsModel"
            ) eu.vendeli.fixtures.____clearClassData(user.id)
            eu.vendeli.fixtures.TgAnnotationsModel::test2.invoke(
                inst,
            )
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.TgAnnotationsModel",
                "test2",
                zeroRateLimits,
                eu.vendeli.tgbot.implementations.DefaultGuard::class,
            )
    ),
    "testInp3" to (
        suspendCall { classManager, update, user, bot, parameters ->
            if (user != null && bot.update.userClassSteps[user.id] != "eu.vendeli.fixtures.TestObj")
                eu.vendeli.fixtures.____clearClassData(user.id)
            eu.vendeli.fixtures.TestObj::test2.invoke()
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.TestObj",
                "test2",
                zeroRateLimits,
                eu.vendeli.tgbot.implementations.DefaultGuard::class,
            )
    ),
    "eu.vendeli.fixtures.Conversation.Name" to (
        suspendCall {
                classManager,
                update,
                user,
                bot,
                parameters,
            ->
            if (user == null) return@suspendCall Unit
            val inst = classManager.getInstance(eu.vendeli.fixtures.Conversation.Name::class) as
                eu.vendeli.fixtures.Conversation.Name
            inst.beforeAction?.invoke(user, update, bot)
            val nextLink: String? = """eu.vendeli.fixtures.Conversation.Age"""
            val breakPoint = inst.breakCondition?.invoke(user, update, bot) ?: false
            if (breakPoint && inst.retryAfterBreak) {
                bot.inputListener[user] = "eu.vendeli.fixtures.Conversation.Name"
            }
            if (breakPoint) {
                inst.breakAction(user, update, bot)
                return@suspendCall Unit
            }
            val chainInst = classManager.getInstance(eu.vendeli.fixtures.Conversation::class) as
                eu.vendeli.fixtures.Conversation
            chainInst.clearAllState(user, "eu.vendeli.fixtures.Conversation")
            chainInst.setState(
                user,
                "eu.vendeli.fixtures.Conversation.Name",
                StoredState(
                    update,
                    inst.stateSelector,
                ),
            )
            if (user != null && bot.update.userClassSteps[user.id] != "eu.vendeli.fixtures.Conversation")
                eu.vendeli.fixtures.____clearClassData(user.id)
            inst.action(user, update, bot)
            if (nextLink != null) bot.inputListener[user] = nextLink
            inst.afterAction?.invoke(user, update, bot)
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.Conversation",
                "Name",
                zeroRateLimits,
                eu.vendeli.tgbot.implementations.DefaultGuard::class,
            )
    ),
    "eu.vendeli.fixtures.Conversation.Age" to (
        suspendCall {
                classManager,
                update,
                user,
                bot,
                parameters,
            ->
            if (user == null) return@suspendCall Unit
            val inst = classManager.getInstance(eu.vendeli.fixtures.Conversation.Age::class) as
                eu.vendeli.fixtures.Conversation.Age
            inst.beforeAction?.invoke(user, update, bot)
            val nextLink: String? = null
            val breakPoint = inst.breakCondition?.invoke(user, update, bot) ?: false
            if (breakPoint && inst.retryAfterBreak) {
                bot.inputListener[user] = "eu.vendeli.fixtures.Conversation.Age"
            }
            if (breakPoint) {
                inst.breakAction(user, update, bot)
                return@suspendCall Unit
            }
            val chainInst = classManager.getInstance(eu.vendeli.fixtures.Conversation::class) as
                eu.vendeli.fixtures.Conversation
            chainInst.setState(
                user,
                "eu.vendeli.fixtures.Conversation.Age",
                StoredState(
                    update,
                    inst.stateSelector,
                ),
            )
            if (user != null && bot.update.userClassSteps[user.id] != "eu.vendeli.fixtures.Conversation")
                eu.vendeli.fixtures.____clearClassData(user.id)
            inst.action(user, update, bot)
            if (nextLink != null) bot.inputListener[user] = nextLink
            inst.afterAction?.invoke(user, update, bot)
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.Conversation",
                "Age",
                zeroRateLimits,
                eu.vendeli.tgbot.implementations.DefaultGuard::class,
            )
    ),
)

private val __TG_COMMONS0: Map<CommonMatcher, Invocable> = mapOf(
    CommonMatcher.String(
        value = "common",
        filter = eu.vendeli.tgbot.implementations.DefaultFilter::class,
        setOf(),
    ) to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class) as
                eu.vendeli.fixtures.TgAnnotationsModel
            if (user != null &&
                bot.update.userClassSteps[user.id] !=
                "eu.vendeli.fixtures.TgAnnotationsModel"
            )
                eu.vendeli.fixtures.____clearClassData(user.id)
            eu.vendeli.fixtures.TgAnnotationsModel::common.invoke(
                inst,
            )
        }
            to InvocationMeta("eu.vendeli.fixtures.TgAnnotationsModel", "common", zeroRateLimits)
    ),
    CommonMatcher.String(
        value = "common2",
        filter = eu.vendeli.tgbot.implementations.DefaultFilter::class,
        setOf(),
    ) to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class) as
                eu.vendeli.fixtures.TgAnnotationsModel
            if (user != null &&
                bot.update.userClassSteps[user.id] !=
                "eu.vendeli.fixtures.TgAnnotationsModel"
            )
                eu.vendeli.fixtures.____clearClassData(user.id)
            eu.vendeli.fixtures.TgAnnotationsModel::common.invoke(
                inst,
            )
        }
            to InvocationMeta("eu.vendeli.fixtures.TgAnnotationsModel", "common", zeroRateLimits)
    ),
    CommonMatcher.Regex(
        value = Regex("test colou?r"),
        filter =
            eu.vendeli.tgbot.implementations.DefaultFilter::class,
        setOf(),
    ) to (
        suspendCall {
                classManager,
                update,
                user,
                bot,
                parameters,
            ->
            val inst = classManager.getInstance(eu.vendeli.fixtures.RegexCommands::class) as
                eu.vendeli.fixtures.RegexCommands
            val param0 = bot
            if (user != null &&
                bot.update.userClassSteps[user.id] !=
                "eu.vendeli.fixtures.RegexCommands"
            ) eu.vendeli.fixtures.____clearClassData(user.id)
            eu.vendeli.fixtures.RegexCommands::testR.invoke(
                inst,
                param0,
            )
        }
            to InvocationMeta("eu.vendeli.fixtures.RegexCommands", "testR", zeroRateLimits)
    ),
)

private val __TG_UPDATE_TYPES0: Map<UpdateType, InvocationLambda> = mapOf(
    UpdateType.MESSAGE to suspendCall { classManager, update, user, bot, parameters ->
        val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class) as
            eu.vendeli.fixtures.TgAnnotationsModel
        eu.vendeli.fixtures.TgAnnotationsModel::updateHandler.invoke(
            inst,
        )
    },
    UpdateType.CALLBACK_QUERY to suspendCall { classManager, update, user, bot, parameters ->
        val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class) as
            eu.vendeli.fixtures.TgAnnotationsModel
        eu.vendeli.fixtures.TgAnnotationsModel::updateHandler.invoke(
            inst,
        )
    },
)

private val __TG_UNPROCESSED0: InvocationLambda? = suspendCall {
        classManager,
        update,
        user,
        bot,
        parameters,
    ->
    val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class) as
        eu.vendeli.fixtures.TgAnnotationsModel
    eu.vendeli.fixtures.TgAnnotationsModel::test3.invoke(
        inst,
    )
}

internal val __ACTIVITIES: Map<String, List<Any?>> = mapOf(
    "eu.vendeli.fixtures" to
        listOf(
            __TG_COMMANDS0,
            __TG_INPUTS0,
            __TG_COMMONS0,
            __TG_UPDATE_TYPES0,
            __TG_UNPROCESSED0,
        ),
)
