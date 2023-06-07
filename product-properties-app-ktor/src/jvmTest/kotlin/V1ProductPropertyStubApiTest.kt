package com.crowdproj.marketplace.app

import com.crowdproj.marketplace.api.v1.models.*
import com.crowdproj.marketplace.app.auth.addAuth
import com.crowdproj.marketplace.app.base.KtorAuthConfig
import com.crowdproj.marketplace.app.helpers.testSettings
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class V1ProductPropertyStubApiTest {
    @Test
    fun create() = testApplication {
        application {
            moduleJvm(appSettings = testSettings())
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
            addAuth(config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            val requestJson = Json.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = Json.decodeFromString<ProductPropertyCreateResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("1", responseObj.productProperty?.id)
        assertEquals("LENGTH", responseObj.productProperty?.name)
        assertEquals("length desc", responseObj.productProperty?.description)
        assertEquals("100", responseObj.productProperty?.unitMain)
    }

    @Test
    fun read() = testApplication {
        application {
            moduleJvm(appSettings = testSettings())
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
            addAuth(config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            val requestJson = Json.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = Json.decodeFromString<ProductPropertyReadResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(3, responseObj.productProperties?.size)
        assertEquals("3", responseObj.productProperties?.last()?.id)
        assertEquals("Height", responseObj.productProperties?.last()?.name)
    }

    @Test
    fun update() = testApplication {
        application {
            moduleJvm(appSettings = testSettings())
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
            addAuth(config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            val requestJson = Json.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = Json.decodeFromString<ProductPropertyUpdateResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("1", responseObj.productProperty?.id)
        assertEquals("LENGTH", responseObj.productProperty?.name)
        assertEquals("length desc", responseObj.productProperty?.description)
        assertEquals("100", responseObj.productProperty?.unitMain)
        assertEquals(false, responseObj.productProperty?.deleted)
    }

    @Test
    fun delete() = testApplication {
        application {
            moduleJvm(appSettings = testSettings())
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
            addAuth(config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            val requestJson = Json.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = Json.decodeFromString<ProductPropertyDeleteResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("3", responseObj.productProperty?.id)
        assertEquals("Height", responseObj.productProperty?.name)
        assertEquals(true, responseObj.productProperty?.deleted)
    }

    @Test
    fun search() = testApplication {
        application {
            moduleJvm(appSettings = testSettings())
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
            addAuth(config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            val requestJson = Json.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = Json.decodeFromString<ProductPropertySearchResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(3, responseObj.productProperties?.size)
        assertEquals("3", responseObj.productProperties?.last()?.id)
        assertEquals("Height", responseObj.productProperties?.last()?.name)
    }
}