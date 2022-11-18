package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.*
import eu.vendeli.tgbot.types.internal.ActionContext
import eu.vendeli.tgbot.types.internal.CommandContext
import eu.vendeli.tgbot.types.internal.CommandSelector
import eu.vendeli.tgbot.types.internal.SingleInputChain

typealias OnMessageAction = suspend ActionContext<Message>.() -> Unit
typealias OnEditedMessageAction = suspend ActionContext<Message>.() -> Unit
typealias OnPollAnswerAction = suspend ActionContext<PollAnswer>.() -> Unit
typealias OnCallbackQueryAction = suspend ActionContext<CallbackQuery>.() -> Unit
typealias OnPollAction = suspend ActionContext<Poll>.() -> Unit
typealias OnChatJoinRequestAction = suspend ActionContext<ChatJoinRequest>.() -> Unit
typealias OnChatMemberAction = suspend ActionContext<ChatMemberUpdated>.() -> Unit
typealias OnMyChatMemberAction = suspend ActionContext<ChatMemberUpdated>.() -> Unit
typealias OnChannelPostAction = suspend ActionContext<Message>.() -> Unit
typealias OnEditedChannelPostAction = suspend ActionContext<Message>.() -> Unit
typealias OnChosenInlineResultAction = suspend ActionContext<ChosenInlineResult>.() -> Unit
typealias OnInlineQueryAction = suspend ActionContext<InlineQuery>.() -> Unit
typealias OnPreCheckoutQueryAction = suspend ActionContext<PreCheckoutQuery>.() -> Unit
typealias OnShippingQueryAction = suspend ActionContext<ShippingQuery>.() -> Unit
typealias WhenNotHandledAction = suspend Update.() -> Unit

typealias InputActions = MutableMap<String, SingleInputChain>
typealias CommandActions = MutableMap<CommandSelector, suspend CommandContext.() -> Unit>