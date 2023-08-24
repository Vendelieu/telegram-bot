package eu.vendeli.tgbot.types.internal.options

data class ContactOptions(
    var lastName: String? = null,
    var vcard: String? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Long? = null,
) : OptionsCommon
