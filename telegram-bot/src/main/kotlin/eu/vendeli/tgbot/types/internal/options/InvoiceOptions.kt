package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ReplyParameters
import kotlinx.serialization.Serializable

@Serializable
data class InvoiceOptions(
    var photoUrl: String? = null,
    var photoHeight: Int? = null,
    var photoWidth: Int? = null,
    var isFlexible: Boolean? = null,
    var needName: Boolean? = null,
    var needEmail: Boolean? = null,
    var needPhoneNumber: Boolean? = null,
    var needShippingAddress: Boolean? = null,
    var startParameter: String? = null,
    var suggestedTipAmounts: List<Int>? = null,
    var providerData: String? = null,
    var maxTipAmount: Int? = null,
    override var protectContent: Boolean? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var messageThreadId: Int? = null,
) : OptionsCommon
