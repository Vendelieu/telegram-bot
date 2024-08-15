package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.chain.Link

fun interface Action {
    suspend fun invoke(user: User, update: ProcessedUpdate, bot: TelegramBot)
}

fun interface BreakCondition {
    suspend fun invoke(user: User, update: ProcessedUpdate, bot: TelegramBot): Boolean
}

abstract class ChainLink : Link<Unit> {
    override val retryAfterBreak = true
    override val breakCondition: BreakCondition? = null
    override val beforeAction: Action? = null
    override val afterAction: Action? = null
    open val stateSelector: StateSelector = StateSelector.Text
}
