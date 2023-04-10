import ch.qos.logback.classic.Level.TRACE
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.internal.HttpLogLevel
import eu.vendeli.tgbot.types.internal.Response
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.AnnotationSpec
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlin.random.Random

@Suppress("VariableNaming", "PropertyName", "PrivatePropertyName")
abstract class BotTestContext(
    private val withPreparedBot: Boolean = true,
    private val mockHttp: Boolean = false,
    private val mockedUpdates: List<Update>? = null,
) : AnnotationSpec() {
    protected lateinit var bot: TelegramBot
    protected var classloader: ClassLoader = Thread.currentThread().contextClassLoader

    private val TOKEN by lazy { System.getenv("BOT_TOKEN") }
    protected val TG_ID by lazy { System.getenv("TELEGRAM_ID").toLong() }
    protected val BOT_ID by lazy { TOKEN.substringBefore(':').toLong() }

    protected val RANDOM_PIC_URL = "https://picsum.photos/10"
    protected val RANDOM_PIC by lazy { runBlocking { bot.httpClient.get(RANDOM_PIC_URL).readBytes() } }

    @BeforeAll
    fun prepareTestBot() {
        if (withPreparedBot) bot = TelegramBot(TOKEN, "eu.vendeli") {
            logging {
                botLogLevel = TRACE
                httpLogLevel = HttpLogLevel.ALL
            }
            httpClient {
                maxRequestRetry = 0
            }
        }

        if (mockHttp) doMockHttp()
    }

    fun doMockHttp(messageText: String = "/start", updates: List<Update>? = mockedUpdates) {
        val testMsg = Message(
            Random.nextLong(),
            from = User(1, false, "Test"),
            chat = Chat(1, ChatType.Private),
            date = Random.nextInt(),
            text = messageText,
        )
        val apiResponse = Response.Success(
            updates ?: listOf(
                Update(Random.nextInt(), testMsg),
            ),
        )
        bot.httpClient = HttpClient(
            MockEngine {
                respond(
                    content = ByteReadChannel(TelegramBot.mapper.writeValueAsBytes(apiResponse)),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )
    }
}
