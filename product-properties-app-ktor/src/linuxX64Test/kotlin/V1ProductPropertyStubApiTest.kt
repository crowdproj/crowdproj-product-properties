import com.crowdproj.marketplace.api.v1.apiV1Mapper
import com.crowdproj.marketplace.api.v1.models.*
import com.crowdproj.marketplace.app.module
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertEquals

class V1ProductPropertyStubApiTest {
    @Test
    fun create() = testApplication {
        application {
            module()
        }
        val response = client.post("/v1/product/property/create") {
            val requestObj = ProductPropertyCreateRequest(
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
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<ProductPropertyCreateResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("1", responseObj.productProperty?.id)
        assertEquals("Length", responseObj.productProperty?.name)
        assertEquals("Length description", responseObj.productProperty?.description)
        assertEquals("100", responseObj.productProperty?.unitMain)
    }

    @Test
    fun read() = testApplication {
        application {
            module()
        }
        val response = client.post("/v1/product/properties/read") {
            val requestObj = ProductPropertyReadRequest(
                requestId = "12345",
                productPropertyIds = listOf("1,2,3"),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.STUB,
                    stub = CpRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<ProductPropertyReadResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(3, responseObj.productProperties?.size)
        assertEquals("3", responseObj.productProperties?.last()?.id)
        assertEquals("Height", responseObj.productProperties?.last()?.name)
    }

    @Test
    fun update() = testApplication {
        application {
            module()
        }
        val response = client.post("/v1/product/property/update") {
            val requestObj = ProductPropertyUpdateRequest(
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
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<ProductPropertyUpdateResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("1", responseObj.productProperty?.id)
        assertEquals("Length", responseObj.productProperty?.name)
        assertEquals("Length description", responseObj.productProperty?.description)
        assertEquals("100", responseObj.productProperty?.unitMain)
        assertEquals(false, responseObj.productProperty?.deleted)
    }

    @Test
    fun delete() = testApplication {
        application {
            module()
        }
        val response = client.post("/v1/product/property/delete") {
            val requestObj = ProductPropertyDeleteRequest(
                requestId = "12345",
                productPropertyId = "3",
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.STUB,
                    stub = CpRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<ProductPropertyDeleteResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("3", responseObj.productProperty?.id)
        assertEquals("Height", responseObj.productProperty?.name)
        assertEquals(true, responseObj.productProperty?.deleted)
    }

    @Test
    fun search() = testApplication {
        application {
            module()
        }
        val response = client.post("/v1/product/property/search") {
            val requestObj = ProductPropertySearchRequest(
                requestId = "12345",
                productPropertyFilter = ProductPropertySearchFilter(
                    description = "description"
                ),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.STUB,
                    stub = CpRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<ProductPropertySearchResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(3, responseObj.productProperties?.size)
        assertEquals("3", responseObj.productProperties?.last()?.id)
        assertEquals("Height", responseObj.productProperties?.last()?.name)
    }
}