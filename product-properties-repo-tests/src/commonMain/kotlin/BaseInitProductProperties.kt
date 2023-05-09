package com.crowdproj.marketplace.repository.tests

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductPropertyLock
import com.crowdproj.marketplace.common.models.ProductUnitId

abstract class BaseInitProductProperties(val op: String) : IInitObjects<ProductProperty> {
    open val lockOld: ProductPropertyLock = ProductPropertyLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: ProductPropertyLock = ProductPropertyLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        unitId: ProductUnitId = ProductUnitId("unit-id-stub"),
        lock: ProductPropertyLock = lockOld,
    ) = ProductProperty(
        id = ProductPropertyId("prop-repo-$op-$suf"),
        name = "$suf stub",
        description = "$suf stub description",
        unitMain = unitId,
        units = listOf(unitId),
        lock = lock
    )
}