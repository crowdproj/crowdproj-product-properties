package com.crowdproj.marketplace.auth

import com.crowdproj.marketplace.common.permissions.ProductPropertyPermissionClient
import com.crowdproj.marketplace.common.permissions.PropPrincipalRelations
import com.crowdproj.marketplace.common.permissions.PropUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<PropUserPermissions>,
    relations: Iterable<PropPrincipalRelations>,
) = mutableSetOf<ProductPropertyPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // READ
    PropUserPermissions.READ_OWN to mapOf(
        PropPrincipalRelations.OWN to ProductPropertyPermissionClient.READ
    ),
    PropUserPermissions.READ_PUBLIC to mapOf(
        PropPrincipalRelations.PUBLIC to ProductPropertyPermissionClient.READ
    ),


    // UPDATE
    PropUserPermissions.UPDATE_OWN to mapOf(
        PropPrincipalRelations.OWN to ProductPropertyPermissionClient.UPDATE
    ),
    PropUserPermissions.UPDATE_PUBLIC to mapOf(
        PropPrincipalRelations.PUBLIC to ProductPropertyPermissionClient.UPDATE
    ),

    // DELETE
    PropUserPermissions.DELETE_OWN to mapOf(
        PropPrincipalRelations.OWN to ProductPropertyPermissionClient.DELETE
    ),
    PropUserPermissions.DELETE_PUBLIC to mapOf(
        PropPrincipalRelations.PUBLIC to ProductPropertyPermissionClient.DELETE
    ),

    // SEARCH
    PropUserPermissions.SEARCH_OWN to mapOf(
        PropPrincipalRelations.OWN to ProductPropertyPermissionClient.SEARCH
    ),
    PropUserPermissions.SEARCH_PUBLIC to mapOf(
        PropPrincipalRelations.PUBLIC to ProductPropertyPermissionClient.SEARCH
    ),
)
