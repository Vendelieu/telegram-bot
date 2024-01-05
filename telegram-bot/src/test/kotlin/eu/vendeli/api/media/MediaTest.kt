package eu.vendeli.api.media

import BotTestContext
import eu.vendeli.tgbot.api.media.animation
import eu.vendeli.tgbot.api.media.audio
import eu.vendeli.tgbot.api.media.document
import eu.vendeli.tgbot.api.media.sendAnimation
import eu.vendeli.tgbot.api.media.sendAudio
import eu.vendeli.tgbot.api.media.sendDocument
import eu.vendeli.tgbot.api.media.sendSticker
import eu.vendeli.tgbot.api.media.sendVideo
import eu.vendeli.tgbot.api.media.sendVideoNote
import eu.vendeli.tgbot.api.media.sendVoice
import eu.vendeli.tgbot.api.media.sticker
import eu.vendeli.tgbot.api.media.video
import eu.vendeli.tgbot.api.media.videoNote
import eu.vendeli.tgbot.api.media.voice
import eu.vendeli.utils.LOREM
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class MediaTest : BotTestContext() {
    @Test
    suspend fun `audio method test`() {
        val lorem = LOREM.AUDIO
        val textResult = sendAudio { lorem.text }.sendReturning(TG_ID, bot).shouldSuccess()
        val bytesResult = audio(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = audio(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = audio(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                audio.shouldNotBeNull()
                audio?.fileName shouldBe "audio.mp3"
                audio?.duration shouldBe 121
            }
        }
    }

    @Test
    suspend fun `animation method test`() {
        val lorem = LOREM.ANIMATION
        val textResult = sendAnimation { lorem.text }.sendReturning(TG_ID, bot).shouldSuccess()
        val bytesResult = animation(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = animation(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = animation(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                animation.shouldNotBeNull()
                animation?.fileName shouldBe "animated-parabola.gif.mp4"
            }
        }
    }

    @Test
    suspend fun `document method test`() {
        val lorem = LOREM.DOCUMENT
        val textResult = sendDocument { lorem.text }.sendReturning(TG_ID, bot).shouldSuccess()
        val bytesResult = document(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = document(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = document(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                document.shouldNotBeNull()
                document?.fileName shouldBe "Lorem_ipsum.pdf"
            }
        }
    }

    @Test
    suspend fun `video method test`() {
        val lorem = LOREM.VIDEO
        val textResult = sendVideo { lorem.text }.sendReturning(TG_ID, bot).shouldSuccess()
        val bytesResult = video(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = video(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = video(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        val result = sendVideo { LOREM.VIDEO.text }.sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                video.shouldNotBeNull()
                video?.fileName shouldBe "small.mp4"
            }
        }
    }

    @Test
    suspend fun `video note method test`() {
        val lorem = LOREM.VIDEO_NOTE
        val textResult = sendVideoNote { lorem.text }.sendReturning(TG_ID, bot).shouldSuccess()
        val bytesResult = videoNote(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = videoNote(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = videoNote(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                videoNote.shouldNotBeNull()
            }
        }
    }

    @Test
    suspend fun `voice method test`() {
        val lorem = LOREM.VOICE
        val textResult = sendVoice { lorem.text }.sendReturning(TG_ID, bot).shouldSuccess()
        val bytesResult = voice(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = voice(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = voice(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                voice.shouldNotBeNull()
            }
        }
    }

    @Test
    suspend fun `sticker method test`() {
        val lorem = LOREM.STICKER
        val textResult = sendSticker { lorem.text }.sendReturning(TG_ID, bot).shouldSuccess()
        val bytesResult = sticker(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = sticker(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = sticker(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                sticker.shouldNotBeNull()
            }
        }
    }
}
