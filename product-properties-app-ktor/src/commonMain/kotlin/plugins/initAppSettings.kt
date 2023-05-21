package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.app.PropAppSettings
import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.logging.common.PropLoggerProvider
import com.crowdproj.marketplace.repository.stubs.PropRepoStub
import io.ktor.server.application.*

fun Application.initAppSettings(): PropAppSettings = PropAppSettings(
    appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
    corSettings = PropCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf(PropDbType.TEST),
        repoProd = getDatabaseConf(PropDbType.PROD),
        repoStub = PropRepoStub()
    ),
    processor = ProductPropertyProcessor()
)

expect fun Application.getLoggerProviderConf(): PropLoggerProvider