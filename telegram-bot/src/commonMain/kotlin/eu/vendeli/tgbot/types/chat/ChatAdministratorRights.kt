package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatAdministratorRights(
    val isAnonymous: Boolean,
    val canManageChat: Boolean,
    val canDeleteMessages: Boolean,
    val canRestrictMembers: Boolean,
    val canPromoteMembers: Boolean,
    val canChangeInfo: Boolean,
    val canInviteUsers: Boolean,
    val canPostMessages: Boolean? = null,
    val canEditMessages: Boolean? = null,
    val canPinMessages: Boolean? = null,
    val canManageTopics: Boolean? = null,
    val canPostStories: Boolean? = null,
    val canEditStories: Boolean? = null,
    val canDeleteStories: Boolean? = null,
)
