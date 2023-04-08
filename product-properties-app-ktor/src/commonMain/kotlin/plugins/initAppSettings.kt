package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.app.PropAppSettings
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.logging.common.PropLoggerProvider
import io.ktor.server.application.*

fun Application.initAppSettings(): PropAppSettings = PropAppSettings(
    appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
    corSettings = PropCorSettings(
        loggerProvider = getLoggerProviderConf()
    ),
)

expect fun Application.getLoggerProviderConf(): PropLoggerProvider