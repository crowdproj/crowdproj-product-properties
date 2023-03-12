package com.crowdproj.marketplace.app

import com.crowdproj.marketplace.app.controller.v1ProductProperty
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(CIO, port = 8080) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(Routing)
    install(ContentNegotiation) {
        json(Json {
            isLenient = true
            prettyPrint = true
            ignoreUnknownKeys = true
        })
    }

    routing {
        route("v1") {
            v1ProductProperty()
        }
    }
}