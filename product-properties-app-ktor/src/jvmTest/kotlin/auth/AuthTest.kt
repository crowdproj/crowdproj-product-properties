package com.crowdproj.marketplace.app.auth

import com.crowdproj.marketplace.app.base.KtorAuthConfig
import com.crowdproj.marketplace.app.helpers.testSettings
import com.crowdproj.marketplace.app.moduleJvm
import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        application {
            moduleJvm(testSettings())
        }

        val response = client.post("/v1/product/property/create") {
            addAuth(config = KtorAuthConfig.TEST.copy(audience = "invalid"))
        }
        assertEquals(401, response.status.value)
    }
}
