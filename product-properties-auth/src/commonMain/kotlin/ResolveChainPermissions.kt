package com.crowdproj.marketplace.auth

import com.crowdproj.marketplace.common.permissions.PropUserGroups
import com.crowdproj.marketplace.common.permissions.PropUserPermissions

fun resolveChainPermissions(
    groups: Iterable<PropUserGroups>,
) = mutableSetOf<PropUserPermissions>()
    .apply {
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits = mapOf(
    PropUserGroups.USER to setOf(
        PropUserPermissions.READ_OWN,
        PropUserPermissions.READ_PUBLIC,
        PropUserPermissions.CREATE_OWN,
        PropUserPermissions.UPDATE_OWN,
        PropUserPermissions.DELETE_OWN,
        PropUserPermissions.SEARCH_OWN,
        PropUserPermissions.SEARCH_PUBLIC,
    ),
    PropUserGroups.ADMIN_MP to PropUserPermissions.values().toSet(),
    PropUserGroups.TEST to setOf(),
    PropUserGroups.BAN_MP to setOf(),
)

private val groupPermissionsDenys = mapOf(
    PropUserGroups.USER to setOf(),
    PropUserGroups.ADMIN_MP to setOf(),
    PropUserGroups.TEST to setOf(),
    PropUserGroups.BAN_MP to setOf(
        PropUserPermissions.UPDATE_OWN,
        PropUserPermissions.CREATE_OWN,
    ),
)