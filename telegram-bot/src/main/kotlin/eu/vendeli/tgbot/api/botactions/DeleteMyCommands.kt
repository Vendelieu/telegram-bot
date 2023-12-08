@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeleteMyCommandsAction(
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("deleteMyCommands")
    override val returnType = getReturnType()

    init {
        if (scope != null) parameters["scope"] = scope
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun deleteMyCommands(languageCode: String? = null, scope: BotCommandScope? = null) =
    DeleteMyCommandsAction(scope, languageCode)
