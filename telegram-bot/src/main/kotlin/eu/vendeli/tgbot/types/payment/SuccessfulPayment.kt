package eu.vendeli.tgbot.types.payment

import eu.vendeli.tgbot.types.internal.Currency
import kotlinx.serialization.Serializable

@Serializable
data class SuccessfulPayment(
    val currency: Currency,
    val totalAmount: Int,
    val invoicePayload: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo,
    val telegramPaymentChargeId: String,
    val providerPaymentChargeId: String,
)
