package eu.vendeli.ksp.dto

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import eu.vendeli.tgbot.types.internal.configuration.RateLimits

sealed class CommonAnnotationValue {
    abstract val value: Any

    class String(override val value: kotlin.String) : CommonAnnotationValue()
    class Regex(override val value: kotlin.text.Regex) : CommonAnnotationValue()

    fun toCommonMatcher(filter: kotlin.String) = when (this) {
        is String -> "CommonMatcher.String(value = \"$value\", filter = $filter::class)"
        is Regex -> "CommonMatcher.Regex(value = Regex(\"$value\"${
            value.options.takeIf { it.isNotEmpty() }?.joinToString(prefix = " ,") { "RegexOption.$it" } ?: ""
        }), filter = $filter::class)"
    }
}

data class CommonAnnotationData(
    val funQualifier: String,
    val funSimpleName: String,
    val value: CommonAnnotationValue,
    val filter: String,
    val priority: Int,
    val rateLimits: RateLimits,
    val funDeclaration: KSFunctionDeclaration,
)
