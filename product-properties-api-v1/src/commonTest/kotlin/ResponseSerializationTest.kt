import com.crowdproj.marketplace.api.v1.apiV1Mapper
import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse
import com.crowdproj.marketplace.api.v1.models.ProductPropertyCreateResponse
import com.crowdproj.marketplace.api.v1.models.ProductPropertyResponseObject
import com.crowdproj.marketplace.api.v1.models.ResponseResult
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response: IProductPropertyResponse = ProductPropertyCreateResponse(
        responseType = "create",
        requestId = "123",
        result = ResponseResult.SUCCESS,
        productProperty = ProductPropertyResponseObject(
            id = "300",
            name = "product property name",
            description = "product property description",
            unitMain = "100",
            units = listOf("100", "200", "300")
        )

    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.encodeToString(response)

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
        val json = apiV1Mapper.encodeToString(response)
        val obj = apiV1Mapper.decodeFromString(json) as ProductPropertyCreateResponse

        assertEquals(response, obj)
    }
}