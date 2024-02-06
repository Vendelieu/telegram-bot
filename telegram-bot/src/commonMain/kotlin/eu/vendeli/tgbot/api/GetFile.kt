@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetFileAction(fileId: String) : SimpleAction<File>() {
    override val method = TgMethod("getFile")
    override val returnType = getReturnType()

    init {
        parameters["file_id"] = fileId.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun getFile(fileId: String) = GetFileAction(fileId)
