package com.crowdproj.marketplace.common.permissions

import com.crowdproj.marketplace.common.models.PropUserId

data class PropPrincipalModel(
    val id: PropUserId = PropUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<PropUserGroups> = emptySet()
) {
    companion object {
        val NONE = PropPrincipalModel()
    }
}
