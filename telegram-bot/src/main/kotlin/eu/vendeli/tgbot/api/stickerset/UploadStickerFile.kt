@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.File
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class UploadStickerFileAction(private val pngSticker: ImplicitFile<*>) : MediaAction<File>, ActionState() {
    override val method: TgMethod = TgMethod("uploadStickerFile")
    override val returnType = getReturnType()

    override val MediaAction<File>.defaultType: MediaContentType
        get() = MediaContentType.ImagePng
    override val MediaAction<File>.media: ImplicitFile<*>
        get() = pngSticker
    override val MediaAction<File>.dataField: String
        get() = "png_sticker"
}

fun uploadStickerFile(string: () -> String) = UploadStickerFileAction(ImplicitFile.FromString(string()))
fun uploadStickerFile(ba: ByteArray) = UploadStickerFileAction(ImplicitFile.FromByteArray(ba))

fun uploadStickerFile(file: java.io.File) = UploadStickerFileAction(ImplicitFile.FromFile(file))
