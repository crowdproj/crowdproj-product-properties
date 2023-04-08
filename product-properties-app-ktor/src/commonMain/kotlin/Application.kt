package com.crowdproj.marketplace.app

import com.crowdproj.marketplace.app.controller.v1ProductProperty
import com.crowdproj.marketplace.app.controller.wsHandlerV1
import com.crowdproj.marketplace.app.plugins.initAppSettings
import com.crowdproj.marketplace.app.plugins.initPlugins
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

fun Application.module(appSettings: PropAppSettings = initAppSettings()) {
    initPlugins(appSettings)

    routing {
        route("v1") {
            v1ProductProperty(appSettings)

            webSocket("ws") {
                wsHandlerV1(appSettings)
            }
        }
    }
}