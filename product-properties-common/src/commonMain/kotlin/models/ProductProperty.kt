package com.crowdproj.marketplace.common.models

data class ProductProperty(
    var id: ProductPropertyId = ProductPropertyId.NONE,
    var name: String = "",
    var description: String = "",
    var units: List<ProductUnitId> = listOf(),
    var unitMain: ProductUnitId = ProductUnitId.NONE,
    var deleted: Boolean = false,
    var lock: ProductPropertyLock = ProductPropertyLock.NONE,
) {
    fun deepCopy(): ProductProperty = this.copy()
}