package com.crowdproj.marketplace.app.base

import com.crowdproj.marketplace.app.base.KtorAuthConfig.Companion.F_NAME_CLAIM
import com.crowdproj.marketplace.app.base.KtorAuthConfig.Companion.GROUPS_CLAIM
import com.crowdproj.marketplace.app.base.KtorAuthConfig.Companion.ID_CLAIM
import com.crowdproj.marketplace.app.base.KtorAuthConfig.Companion.L_NAME_CLAIM
import com.crowdproj.marketplace.app.base.KtorAuthConfig.Companion.M_NAME_CLAIM
import com.crowdproj.marketplace.common.models.PropUserId
import com.crowdproj.marketplace.common.permissions.PropPrincipalModel
import com.crowdproj.marketplace.common.permissions.PropUserGroups
import io.ktor.server.auth.jwt.*

fun JWTPrincipal?.toModel() = this?.run {
    PropPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { PropUserId(it) } ?: PropUserId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when (it) {
                    "USER" -> PropUserGroups.USER
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: PropPrincipalModel.NONE
