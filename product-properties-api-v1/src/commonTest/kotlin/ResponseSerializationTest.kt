import com.crowdproj.marketplace.api.v1.decodeResponse
import com.crowdproj.marketplace.api.v1.encodeResponse
import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse
import com.crowdproj.marketplace.api.v1.models.ProductPropertyCreateResponse
import com.crowdproj.marketplace.api.v1.models.ProductPropertyResponseObject
import com.crowdproj.marketplace.api.v1.models.ResponseResult
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response: IProductPropertyResponse = ProductPropertyCreateResponse(
        requestId = "123",
        result = ResponseResult.SUCCESS,
        productProperty = ProductPropertyResponseObject(
            id = "300",
            deleted = false,
            name = "product property name",
            description = "product property description",
            unitMain = "100",
            units = listOf("100", "200", "300")
        )

    )

    @Test
    fun serialize() {
        val json = encodeResponse(response)

        println(json)

        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        assertContains(json, Regex("\"requestId\":\\s*\"123\""))
        assertContains(json, Regex("\"result\":\\s*\"success\""))
        assertContains(json, Regex("\"id\":\\s*\"300\""))
        assertContains(json, Regex("\"name\":\\s*\"product property name\""))
        assertContains(json, Regex("\"description\":\\s*\"product property description\""))
        assertContains(json, Regex("\"unitMain\":\\s*\"100\""))
        assertContains(json, Regex("\"units\":\\s*\\[\"100\",\"200\",\"300\"]"))
    }

    @Test
    fun deserialize() {
        val json = encodeResponse(response)
        val obj = decodeResponse(json) as ProductPropertyCreateResponse

        assertEquals(response, obj)
    }
}