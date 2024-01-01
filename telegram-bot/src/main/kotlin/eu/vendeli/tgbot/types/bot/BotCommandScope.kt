package eu.vendeli.tgbot.types.bot

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    defaultImpl = BotCommandScope.Default::class,
)
@JsonSubTypes(
    JsonSubTypes.Type(value = BotCommandScope.Default::class, name = "default"),
    JsonSubTypes.Type(value = BotCommandScope.AllPrivateChats::class, name = "all_private_chats"),
    JsonSubTypes.Type(value = BotCommandScope.AllGroupChats::class, name = "all_group_chats"),
    JsonSubTypes.Type(value = BotCommandScope.AllChatAdministrators::class, name = "all_chat_administrators"),
    JsonSubTypes.Type(value = BotCommandScope.ChatScope::class, name = "chat"),
    JsonSubTypes.Type(value = BotCommandScope.ChatAdministrators::class, name = "chat_administrators"),
    JsonSubTypes.Type(value = BotCommandScope.ChatMember::class, name = "chat_member"),
)
sealed class BotCommandScope(val type: String) {
    data object Default : BotCommandScope(type = "default")
    data object AllPrivateChats : BotCommandScope(type = "all_private_chats")
    data object AllGroupChats : BotCommandScope(type = "all_group_chats")
    data object AllChatAdministrators : BotCommandScope(type = "all_chat_administrators")
    data class ChatScope(val chatId: Long) : BotCommandScope(type = "chat")
    data class ChatAdministrators(val chatId: Long) : BotCommandScope(type = "chat_administrators")
    data class ChatMember(val chatId: Long, val userId: Long) : BotCommandScope(type = "chat_member")
}
