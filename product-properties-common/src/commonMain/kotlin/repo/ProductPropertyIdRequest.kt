package com.crowdproj.marketplace.common.repo

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductPropertyLock

data class ProductPropertyIdRequest(
    val id: ProductPropertyId,
    val lock: ProductPropertyLock = ProductPropertyLock.NONE,
) {
    constructor(productProperty: ProductProperty) : this(productProperty.id, productProperty.lock)
}