package com.crowdproj.marketplace.app

import com.crowdproj.marketplace.app.plugins.initAppSettings
import com.crowdproj.marketplace.logging.jvm.PropLogWrapperLogback
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level

private val clazz = Application::moduleJvm::class.qualifiedName ?: "Application"

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused") // Referenced in application.yaml
fun Application.moduleJvm(appSettings: PropAppSettings = initAppSettings()) {
    module(appSettings)

    install(CallLogging) {
        level = Level.INFO
        val lgr = appSettings
            .corSettings
            .loggerProvider
            .logger(clazz) as? PropLogWrapperLogback
        lgr?.logger?.also { logger = it }
    }
}