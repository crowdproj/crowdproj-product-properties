import com.crowdproj.marketplace.api.v1.apiV1Mapper
import com.crowdproj.marketplace.api.v1.models.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request: IProductPropertyRequest = ProductPropertyCreateRequest(
        requestType = "create",
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
        val json = apiV1Mapper.encodeToString(request)

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
        val json = apiV1Mapper.encodeToString(request)
        val obj = apiV1Mapper.decodeFromString(json) as ProductPropertyCreateRequest

        assertEquals(request, obj)
    }
}