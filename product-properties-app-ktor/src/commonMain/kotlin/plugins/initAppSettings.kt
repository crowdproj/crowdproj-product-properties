package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.app.PropAppSettings
import com.crowdproj.marketplace.app.base.KtorAuthConfig
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
        repoStub = PropRepoStub(),
    ),
    processor = ProductPropertyProcessor(),
    auth = initAppAuth()
)

expect fun Application.getLoggerProviderConf(): PropLoggerProvider

private fun Application.initAppAuth(): KtorAuthConfig = KtorAuthConfig(
    secret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "",
    issuer = environment.config.property("jwt.issuer").getString(),
    audience = environment.config.property("jwt.audience").getString(),
    realm = environment.config.property("jwt.realm").getString(),
    clientId = environment.config.property("jwt.clientId").getString(),
    certUrl = environment.config.propertyOrNull("jwt.certUrl")?.getString(),
)