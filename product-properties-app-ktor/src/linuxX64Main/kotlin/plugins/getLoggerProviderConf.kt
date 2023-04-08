package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.logging.common.PropLoggerProvider
import com.crowdproj.marketplace.logging.kermit.propLoggerKermit
import io.ktor.server.application.*

actual fun Application.getLoggerProviderConf(): PropLoggerProvider = PropLoggerProvider {
    propLoggerKermit(it)
}