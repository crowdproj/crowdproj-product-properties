package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.logging.common.PropLoggerProvider
import com.crowdproj.marketplace.logging.jvm.propLoggerLogback
import com.crowdproj.marketplace.logging.kermit.propLoggerKermit
import io.ktor.server.application.*

actual fun Application.getLoggerProviderConf(): PropLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp" -> PropLoggerProvider { propLoggerKermit(it) }
        "logback", null -> PropLoggerProvider { propLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp and logback")
    }