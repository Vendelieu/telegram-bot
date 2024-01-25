@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CreateNewStickerSetOptions
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class CreateNewStickerSetAction(
    name: String,
    title: String,
    stickerFormat: StickerFormat,
    stickers: List<InputSticker>,
) : MediaAction<Boolean>(), OptionsFeature<CreateNewStickerSetAction, CreateNewStickerSetOptions> {
    override val method = TgMethod("createNewStickerSet")
    override val returnType = getReturnType()
    override val options = CreateNewStickerSetOptions()
    override val idRefField = "user_id"

    init {
        parameters["name"] = name.toJsonElement()
        parameters["title"] = title.toJsonElement()
        parameters["sticker_format"] = stickerFormat.toJsonElement()
        parameters["stickers"] = stickers.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun createNewStickerSet(
    name: String,
    title: String,
    stickerFormat: StickerFormat,
    stickers: List<InputSticker>,
) =
    CreateNewStickerSetAction(name, title, stickerFormat, stickers)
