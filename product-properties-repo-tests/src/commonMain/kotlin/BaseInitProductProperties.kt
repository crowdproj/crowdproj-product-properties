package com.crowdproj.marketplace.repository.tests

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductUnitId

abstract class BaseInitProductProperties(val op: String) : IInitObjects<ProductProperty> {

    fun createInitTestModel(
        suf: String,
        unitId: ProductUnitId = ProductUnitId("unit-id-stub"),
    ) = ProductProperty(
        id = ProductPropertyId("prop-repo-$op-$suf"),
        name = "$suf stub",
        description = "$suf stub description",
        unitMain = unitId,
        units = listOf(unitId)
    )
}