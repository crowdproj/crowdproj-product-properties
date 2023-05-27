package com.crowdproj.marketplace.app

import com.auth0.jwt.JWT
import com.crowdproj.marketplace.app.base.KtorAuthConfig
import com.crowdproj.marketplace.app.base.KtorAuthConfig.Companion.GROUPS_CLAIM
import com.crowdproj.marketplace.app.base.resolveAlgorithm
import com.crowdproj.marketplace.app.controller.v1ProductProperty
import com.crowdproj.marketplace.app.controller.wsHandlerV1
import com.crowdproj.marketplace.app.plugins.initAppSettings
import com.crowdproj.marketplace.app.plugins.initPlugins
import com.crowdproj.marketplace.app.plugins.swagger
import com.crowdproj.marketplace.logging.jvm.PropLogWrapperLogback
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.cio.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level

private val clazz = Application::moduleJvm::class.qualifiedName ?: "Application"

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused") // Referenced in application.yaml
fun Application.moduleJvm(
    appSettings: PropAppSettings = initAppSettings(),
    authConfig: KtorAuthConfig = KtorAuthConfig(environment)
) {
    initPlugins(appSettings)

    install(CallLogging) {
        level = Level.INFO
        val lgr = appSettings
            .corSettings
            .loggerProvider
            .logger(clazz) as? PropLogWrapperLogback
        lgr?.logger?.also { logger = it }
    }
    install(DefaultHeaders)

    install(Authentication) {
        jwt("auth-jwt") {
            realm = authConfig.realm

            verifier {
                val algorithm = it.resolveAlgorithm(authConfig)
                JWT
                    .require(algorithm)
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@moduleJvm.log.error("Groups claim must not be empty in JWT token")
                        null
                    }

                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }

    routing {
        route("v1") {
            authenticate("auth-jwt") {
                v1ProductProperty(appSettings)
            }
            webSocket("ws") {
                wsHandlerV1(appSettings)
            }
        }
        swagger(appSettings)
    }
}