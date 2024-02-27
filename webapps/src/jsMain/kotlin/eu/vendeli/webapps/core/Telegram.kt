package eu.vendeli.webapps.core

import kotlinx.browser.window
import org.w3c.dom.Window

external interface Telegram {
    @Suppress("PropertyName")
    val WebApp: WebApp
}

val Window.Telegram
    get() = asDynamic().Telegram.unsafeCast<Telegram>()

val telegram: Telegram
    get() = window.Telegram
val webApp: WebApp
    get() = telegram.WebApp
