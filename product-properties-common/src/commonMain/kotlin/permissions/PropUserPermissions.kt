package com.crowdproj.marketplace.common.permissions

@Suppress("unused")
enum class PropUserPermissions {
    CREATE_OWN,

    READ_OWN,
    READ_PUBLIC,

    UPDATE_OWN,
    UPDATE_PUBLIC,

    DELETE_OWN,
    DELETE_PUBLIC,

    SEARCH_OWN,
    SEARCH_PUBLIC,
}
