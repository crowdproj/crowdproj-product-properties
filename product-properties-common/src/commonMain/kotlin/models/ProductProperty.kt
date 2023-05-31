package com.crowdproj.marketplace.common.models

import com.crowdproj.marketplace.common.permissions.ProductPropertyPermissionClient
import com.crowdproj.marketplace.common.permissions.PropPrincipalRelations

data class ProductProperty(
    var id: ProductPropertyId = ProductPropertyId.NONE,
    var name: String = "",
    var description: String = "",
    var units: List<ProductUnitId> = listOf(),
    var unitMain: ProductUnitId = ProductUnitId.NONE,
    var deleted: Boolean = false,
    var lock: ProductPropertyLock = ProductPropertyLock.NONE,
    var ownerId: PropUserId = PropUserId.NONE,
    var principalRelations: Set<PropPrincipalRelations> = emptySet(),
    val permissionsClient: MutableSet<ProductPropertyPermissionClient> = mutableSetOf()
) {
    fun deepCopy(): ProductProperty = this.copy()
}