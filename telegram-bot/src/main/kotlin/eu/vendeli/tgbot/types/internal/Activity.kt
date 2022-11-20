package eu.vendeli.tgbot.types.internal

data class Activity(
    val id: String,
    val invocation: Invocation,
    val parameters: Map<String, String>,
    val rateLimits: RateLimits = RateLimits.NOT_LIMITED
)
