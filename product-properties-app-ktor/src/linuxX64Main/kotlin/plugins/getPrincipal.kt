package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.app.PropAppSettings
import com.crowdproj.marketplace.common.models.PropUserId
import com.crowdproj.marketplace.common.permissions.PropPrincipalModel
import com.crowdproj.marketplace.common.permissions.PropUserGroups
import io.ktor.server.application.*

actual fun ApplicationCall.getPrincipal(appSettings: PropAppSettings) = PropPrincipalModel(
    id = PropUserId("user-1"),
    fname = "User",
    mname = "Userovich",
    lname = "Userov",
    groups = setOf(PropUserGroups.TEST, PropUserGroups.USER),
)