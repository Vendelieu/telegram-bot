package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.Serializable

/**
 * This object defines the criteria used to request suitable users. The identifiers of the selected users will be shared with the bot when the corresponding button is pressed. More about requesting users: https://core.telegram.org/bots/features#chat-and-user-selection
 * @property requestId Signed 32-bit identifier of the request that will be received back in the UsersShared object. Must be unique within the message
 * @property userIsBot Optional. Pass True to request bots, pass False to request regular users. If not specified, no additional restrictions are applied.
 * @property userIsPremium Optional. Pass True to request premium users, pass False to request non-premium users. If not specified, no additional restrictions are applied.
 * @property maxQuantity Optional. The maximum number of users to be selected; 1-10. Defaults to 1.
 * Api reference: https://core.telegram.org/bots/api#keyboardbuttonrequestusers
*/
@Serializable
data class KeyboardButtonRequestUsers(
    val requestId: Int,
    val userIsBot: Boolean? = null,
    val userIsPremium: Boolean? = null,
    val maxQuantity: Int? = null,
)
