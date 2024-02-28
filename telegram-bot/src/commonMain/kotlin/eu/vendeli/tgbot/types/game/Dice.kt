package eu.vendeli.tgbot.types.game

import kotlinx.serialization.Serializable

/**
 * This object represents an animated emoji that displays a random value.
 * @property emoji Emoji on which the dice throw animation is based
 * @property value Value of the dice, 1-6 for "🎲", "🎯" and "🎳" base emoji, 1-5 for "🏀" and "⚽" base emoji, 1-64 for "🎰" base emoji
 * Api reference: https://core.telegram.org/bots/api#dice
*/
@Serializable
data class Dice(
    val emoji: String,
    val value: Int,
)
