package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.ImplicitFile

data class VideoOptions(
    var duration: Int? = null,
    var height: Int? = null,
    var width: Int? = null,
    var supportsStreaming: Boolean? = null,
    var thumbnail: ImplicitFile<*>? = null,
    override var fileName: String? = null,
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Long? = null,
    override var hasSpoiler: Boolean? = null,
) : OptionsCommon, OptionsParseMode, FileOptions, MediaSpoiler
