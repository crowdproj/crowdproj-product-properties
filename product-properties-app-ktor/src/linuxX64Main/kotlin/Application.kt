package com.crowdproj.marketplace.app

import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO, environment = applicationEngineEnvironment {
        module {
            module()
        }
        connector {
            port = 8080
            host = "0.0.0.0"
        }
    })
        .start(wait = true)
}

//fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)