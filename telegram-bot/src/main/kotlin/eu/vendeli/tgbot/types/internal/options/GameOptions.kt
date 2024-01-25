package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ReplyParameters
import kotlinx.serialization.Serializable

@Serializable
data class GameOptions(
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var messageThreadId: Int? = null,
) : OptionsCommon
