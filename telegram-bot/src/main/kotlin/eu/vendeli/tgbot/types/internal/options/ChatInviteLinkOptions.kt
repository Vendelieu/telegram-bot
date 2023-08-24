package eu.vendeli.tgbot.types.internal.options

data class ChatInviteLinkOptions(
    var name: String? = null,
    var expireDate: Int? = null,
    var memberLimit: Int? = null,
    var createsJoinRequest: Boolean? = null,
) : Options
