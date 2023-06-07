package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.app.PropAppSettings
import com.crowdproj.marketplace.common.permissions.PropPrincipalModel
import io.ktor.server.application.*

expect fun ApplicationCall.getPrincipal(appSettings: PropAppSettings): PropPrincipalModel