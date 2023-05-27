package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.app.PropAppSettings
import com.crowdproj.marketplace.app.base.toModel
import com.crowdproj.marketplace.common.permissions.PropPrincipalModel
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

actual fun ApplicationCall.getPrincipal(appSettings: PropAppSettings): PropPrincipalModel =
    principal<JWTPrincipal>().toModel()