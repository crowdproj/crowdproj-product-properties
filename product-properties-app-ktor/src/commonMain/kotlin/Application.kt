package com.crowdproj.marketplace.app

import com.crowdproj.marketplace.app.controller.v1ProductProperty
import com.crowdproj.marketplace.app.controller.wsHandlerV1
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            isLenient = true
            prettyPrint = true
        })
    }
    install(WebSockets)

    routing {
        route("v1") {
            v1ProductProperty()

            webSocket("ws") {
                wsHandlerV1()
            }
        }
    }
}