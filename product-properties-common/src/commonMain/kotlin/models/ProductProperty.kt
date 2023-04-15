package com.crowdproj.marketplace.common.models

data class ProductProperty(
    val id: ProductPropertyId = ProductPropertyId.NONE,
    val name: String = "",
    val description: String = "",
    val units: List<ProductUnitId> = listOf(),
    val unitMain: ProductUnitId = ProductUnitId.NONE,
)