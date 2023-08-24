package eu.vendeli.tgbot.types.internal.options

data class AnswerCallbackQueryOptions(
    var text: String? = null,
    var showAlert: Boolean? = null,
    var url: String? = null,
    var cacheTime: Int? = null,
) : Options
