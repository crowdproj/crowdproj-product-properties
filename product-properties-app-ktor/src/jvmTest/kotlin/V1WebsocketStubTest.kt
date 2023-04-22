import com.crowdproj.marketplace.api.v1.apiV1Mapper
import com.crowdproj.marketplace.api.v1.models.*
import io.ktor.client.plugins.websocket.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class V1WebsocketStubTest {

    @Test
    fun createStub() {
        val request = ProductPropertyCreateRequest(
            requestId = "12345",
            productProperty = ProductPropertyCreateObject(
                name = "LENGTH",
                description = "length desc",
                unitMain = "100",
                units = listOf("100", "200", "300"),
            ),
            debug = CpBaseDebug(
                mode = CpRequestDebugMode.STUB,
                stub = CpRequestDebugStubs.SUCCESS
            )
        )

        testMethod<ProductPropertyInitResponse, ProductPropertyCreateResponse>(request) { response ->
            assertEquals("12345", response.requestId)
            assertEquals("1", response.productProperty?.id)
            assertEquals("LENGTH", response.productProperty?.name)
            assertEquals("length desc", response.productProperty?.description)
            assertEquals("100", response.productProperty?.unitMain)
        }
    }

    @Test
    fun readStub() {
        val request = ProductPropertyReadRequest(
            requestId = "12345",
            productPropertyIds = listOf("1,2,3"),
            debug = CpBaseDebug(
                mode = CpRequestDebugMode.STUB,
                stub = CpRequestDebugStubs.SUCCESS
            )
        )

        testMethod<ProductPropertyInitResponse, ProductPropertyReadResponse>(request) { response ->
            assertEquals("12345", response.requestId)
            assertEquals(3, response.productProperties?.size)
            assertEquals("3", response.productProperties?.last()?.id)
            assertEquals("Height", response.productProperties?.last()?.name)
        }
    }

    @Test
    fun updateStub() {
        val request = ProductPropertyUpdateRequest(
            requestId = "12345",
            productProperty = ProductPropertyUpdateObject(
                name = "LENGTH",
                description = "length desc",
                unitMain = "100",
                units = listOf("100", "200", "300"),
            ),
            debug = CpBaseDebug(
                mode = CpRequestDebugMode.STUB,
                stub = CpRequestDebugStubs.SUCCESS
            )
        )

        testMethod<ProductPropertyInitResponse, ProductPropertyUpdateResponse>(request) { response ->
            assertEquals("12345", response.requestId)
            assertEquals("1", response.productProperty?.id)
            assertEquals("LENGTH", response.productProperty?.name)
            assertEquals("length desc", response.productProperty?.description)
            assertEquals("100", response.productProperty?.unitMain)
            assertEquals(false, response.productProperty?.deleted)
        }
    }

    @Test
    fun deleteStub() {
        val request = ProductPropertyDeleteRequest(
            requestId = "12345",
            productPropertyId = "3",
            debug = CpBaseDebug(
                mode = CpRequestDebugMode.STUB,
                stub = CpRequestDebugStubs.SUCCESS
            )
        )

        testMethod<ProductPropertyInitResponse, ProductPropertyDeleteResponse>(request) { response ->
            assertEquals("3", response.productProperty?.id)
            assertEquals("Height", response.productProperty?.name)
            assertEquals(true, response.productProperty?.deleted)
        }
    }

    @Test
    fun searchStub() {
        val request = ProductPropertySearchRequest(
            requestId = "12345",
            productPropertyFilter = ProductPropertySearchFilter(
                description = "description"
            ),
            debug = CpBaseDebug(
                mode = CpRequestDebugMode.STUB,
                stub = CpRequestDebugStubs.SUCCESS
            )
        )

        testMethod<ProductPropertyInitResponse, ProductPropertySearchResponse>(request) { response ->
            assertEquals(3, response.productProperties?.size)
            assertEquals("3", response.productProperties?.last()?.id)
            assertEquals("Height", response.productProperties?.last()?.name)
        }
    }

    private inline fun <reified T, reified R> testMethod(
        request: IProductPropertyRequest,
        crossinline assertBlock: (R) -> Unit
    ) = testApplication {
        val client = createClient {
            install(WebSockets)
        }

        client.webSocket("v1/ws") {
            withTimeout(3000) {
                val incame = incoming.receive() as Frame.Text
                val response = apiV1Mapper.decodeFromString<T>(incame.readText())
                assertIs<ProductPropertyInitResponse>(response)
            }
            send(Frame.Text(apiV1Mapper.encodeToString(request)))
            withTimeout(3000) {
                val incame = incoming.receive() as Frame.Text
                val text = incame.readText()
                val response = apiV1Mapper.decodeFromString<R>(text)

                assertBlock(response)
            }
            close()
        }
    }
}