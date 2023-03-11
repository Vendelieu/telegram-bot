package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope

internal suspend inline fun TelegramBot.makeBunchMediaReq(
    method: TgMethod,
    files: Map<String, ByteArray>,
    parameters: Map<String, Any?>? = null,
    contentType: ContentType,
): HttpResponse = httpClient.post(method.toUrl()) {
    setBody(
        MultiPartFormDataContent(
            formData {
                files.forEach {
                    append(
                        it.key,
                        it.value,
                        Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=${it.key}")
                            append(HttpHeaders.ContentType, contentType)
                        },
                    )
                }
                parameters?.entries?.forEach { entry ->
                    entry.value?.also { append(FormPart(entry.key, TelegramBot.mapper.writeValueAsString(it))) }
                }
            },
        ),
    )
    onUpload { bytesSentTotal, contentLength ->
        logger.trace { "Sent $bytesSentTotal bytes from $contentLength, for $method with $parameters" }
    }
}

@Suppress("LongParameterList")
internal suspend inline fun <T, I : MultipleResponse> TelegramBot.makeBunchMediaRequestAsync(
    method: TgMethod,
    files: Map<String, ByteArray>,
    parameters: Map<String, Any?>? = null,
    contentType: ContentType,
    returnType: Class<T>,
    innerType: Class<I>? = null,
): Deferred<Response<out T>> = coroutineScope {
    val response = makeBunchMediaReq(method, files, parameters, contentType)

    return@coroutineScope handleResponseAsync(response, returnType, innerType)
}

internal suspend inline fun TelegramBot.makeSilentBunchMediaRequest(
    method: TgMethod,
    files: Map<String, ByteArray>,
    parameters: Map<String, Any?>? = null,
    contentType: ContentType,
) {
    makeBunchMediaReq(method, files, parameters, contentType)
}
