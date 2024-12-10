package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.interfaces.helper.ImplicitMediaData
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.utils.toImplicitFile
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@Suppress("OVERRIDE_DEPRECATION")
sealed class InputPaidMedia : ImplicitMediaData {
    @OptIn(ExperimentalSerializationApi::class)
    val type: String by lazy {
        serializer().descriptor.serialName
    }

    @Serializable
    data class Photo(
        override var media: ImplicitFile,
    ) : InputPaidMedia() {
        constructor(media: String) : this(media.toImplicitFile())
        constructor(media: InputFile) : this(media.toImplicitFile())
    }

    @Serializable
    data class Video(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val supportsStreaming: Boolean? = null,
    ) : InputPaidMedia() {
        constructor(
            media: String,
            thumbnail: ImplicitFile? = null,
            width: Int? = null,
            height: Int? = null,
            duration: Int? = null,
            supportsStreaming: Boolean? = null,
        ) : this(media.toImplicitFile(), thumbnail, width, height, duration, supportsStreaming)

        constructor(
            media: InputFile,
            thumbnail: ImplicitFile? = null,
            width: Int? = null,
            height: Int? = null,
            duration: Int? = null,
            supportsStreaming: Boolean? = null,
        ) : this(media.toImplicitFile(), thumbnail, width, height, duration, supportsStreaming)
    }
}
