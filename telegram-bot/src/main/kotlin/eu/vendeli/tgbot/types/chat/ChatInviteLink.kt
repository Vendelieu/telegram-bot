package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User

data class ChatInviteLink(
    val inviteLink: String,
    val creator: User,
    val createsJoinRequest: Boolean,
    val isPrimary: Boolean,
    val isRevoked: Boolean,
    val name: String? = null,
    val expireDate: Int? = null,
    val memberLimit: Int? = null,
    val pendingJoinRequestCount: Int?,
)
