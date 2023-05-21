import com.crowdproj.marketplace.api.v1.decodeRequest
import com.crowdproj.marketplace.api.v1.encodeRequest
import com.crowdproj.marketplace.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request: IProductPropertyRequest = ProductPropertyCreateRequest(
        requestId = "123",
        debug = CpBaseDebug(
            mode = CpRequestDebugMode.STUB,
            stub = CpRequestDebugStubs.SUCCESS
        ),
        productProperty = ProductPropertyCreateObject(
            name = "product property name",
            description = "product property description",
            unitMain = "100",
            units = listOf("100", "200", "300")
        )
    )

    @Test
    fun serialize() {
        val json = encodeRequest(request)

        println(json)

        assertContains(json, Regex("\"requestId\":\\s*\"123\""))
        assertContains(json, Regex("\"name\":\\s*\"product property name\""))
        assertContains(json, Regex("\"description\":\\s*\"product property description\""))
        assertContains(json, Regex("\"unitMain\":\\s*\"100\""))
        assertContains(json, Regex("\"units\":\\s*\\[\"100\",\"200\",\"300\"]"))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"success\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = encodeRequest(request)
        val obj = decodeRequest(json) as ProductPropertyCreateRequest

        assertEquals(request, obj)
    }
}