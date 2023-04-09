package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.app.PropAppSettings
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json

fun Application.initPlugins(appSettings: PropAppSettings) {
    install(ContentNegotiation) {
        json(Json {
            isLenient = true
            prettyPrint = true
        })
    }
    install(WebSockets)

    install(CORS) {
        allowNonSimpleContentTypes = true
        allowSameOrigin = true
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader("*")
        appSettings.appUrls.forEach {
            val split = it.split("://")
            println("$split")
            when (split.size) {
                2 -> allowHost(
                    split[1].split("/")[0]/*.apply { log(module = "app", msg = "COR: $this") }*/,
                    listOf(split[0])
                )

                1 -> allowHost(
                    split[0].split("/")[0]/*.apply { log(module = "app", msg = "COR: $this") }*/,
                    listOf("http", "https")
                )
            }
        }
    }
    install(CachingHeaders)
    install(AutoHeadResponse)
}