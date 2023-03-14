package com.crowdproj.marketplace.app

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.config.yaml.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO, environment = applicationEngineEnvironment {
        module {
            module()
        }
        val appConfig = YamlConfig("src/commonMain/resources/application.yaml")
        if (appConfig != null) {
            config = appConfig
        }
        connector {
            port = appConfig?.port ?: 8080
            host = "0.0.0.0"
        }
    })
        .start(wait = true)
}