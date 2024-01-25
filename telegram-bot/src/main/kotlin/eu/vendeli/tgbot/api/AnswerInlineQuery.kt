@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.AnswerInlineQueryOptions
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class AnswerInlineQueryAction(inlineQueryId: String, results: List<InlineQueryResult>) :
    SimpleAction<Boolean>(),
    OptionsFeature<AnswerInlineQueryAction, AnswerInlineQueryOptions> {
    override val method = TgMethod("answerInlineQuery")
    override val returnType = getReturnType()
    override val options = AnswerInlineQueryOptions()

    init {
        parameters["inline_query_id"] = inlineQueryId.toJsonElement()
        parameters["results"] = results.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun answerInlineQuery(inlineQueryId: String, results: List<InlineQueryResult>) =
    AnswerInlineQueryAction(inlineQueryId, results)
fun answerInlineQuery(inlineQueryId: String, results: ListingBuilder<InlineQueryResult>.() -> Unit) =
    answerInlineQuery(inlineQueryId, ListingBuilder.build(results))

@Suppress("NOTHING_TO_INLINE")
inline fun answerInlineQuery(inlineQueryId: String, vararg result: InlineQueryResult) =
    answerInlineQuery(inlineQueryId, result.asList())
