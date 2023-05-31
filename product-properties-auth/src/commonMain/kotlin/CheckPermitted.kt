package com.crowdproj.marketplace.auth

import com.crowdproj.marketplace.common.models.PropCommand
import com.crowdproj.marketplace.common.permissions.PropPrincipalRelations
import com.crowdproj.marketplace.common.permissions.PropUserPermissions

fun checkPermitted(
    command: PropCommand,
    relations: Iterable<PropPrincipalRelations>,
    permissions: Iterable<PropUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: PropCommand,
    val permission: PropUserPermissions,
    val relation: PropPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = PropCommand.CREATE,
        permission = PropUserPermissions.CREATE_OWN,
        relation = PropPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = PropCommand.READ,
        permission = PropUserPermissions.READ_OWN,
        relation = PropPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = PropCommand.READ,
        permission = PropUserPermissions.READ_PUBLIC,
        relation = PropPrincipalRelations.PUBLIC,
    ) to true,

    // Update
    AccessTableConditions(
        command = PropCommand.UPDATE,
        permission = PropUserPermissions.UPDATE_OWN,
        relation = PropPrincipalRelations.OWN,
    ) to true,

    AccessTableConditions(
        command = PropCommand.UPDATE,
        permission = PropUserPermissions.UPDATE_PUBLIC,
        relation = PropPrincipalRelations.PUBLIC,
    ) to true,

    // Delete
    AccessTableConditions(
        command = PropCommand.DELETE,
        permission = PropUserPermissions.DELETE_OWN,
        relation = PropPrincipalRelations.OWN,
    ) to true,

    AccessTableConditions(
        command = PropCommand.DELETE,
        permission = PropUserPermissions.DELETE_PUBLIC,
        relation = PropPrincipalRelations.PUBLIC,
    ) to true,

    // Search
    AccessTableConditions(
        command = PropCommand.SEARCH,
        permission = PropUserPermissions.SEARCH_OWN,
        relation = PropPrincipalRelations.OWN,
    ) to true,

    AccessTableConditions(
        command = PropCommand.SEARCH,
        permission = PropUserPermissions.SEARCH_PUBLIC,
        relation = PropPrincipalRelations.PUBLIC,
    ) to true,
)
