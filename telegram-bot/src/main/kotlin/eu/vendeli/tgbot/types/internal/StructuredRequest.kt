package eu.vendeli.tgbot.types.internal

internal data class StructuredRequest(
    val command: String,
    val params: Map<String, String>,
)
