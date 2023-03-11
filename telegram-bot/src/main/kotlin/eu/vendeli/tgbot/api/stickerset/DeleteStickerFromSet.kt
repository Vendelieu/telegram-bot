@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeleteStickerFromSetAction(sticker: String) : SimpleAction<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("deleteStickerFromSet")
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker
    }
}

fun deleteStickerFromSet(sticker: String) = DeleteStickerFromSetAction(sticker)
