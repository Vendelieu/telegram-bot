package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class ChatBoost(
    val boostId: String,
    val addDate: Long,
    val expirationDate: Long,
    val source: ChatBoostSource,
)
