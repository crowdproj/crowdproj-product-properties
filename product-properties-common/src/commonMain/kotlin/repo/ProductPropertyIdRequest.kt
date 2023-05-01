package com.crowdproj.marketplace.common.repo

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId

data class ProductPropertyIdRequest(
    val id: ProductPropertyId
) {
    constructor(productProperty: ProductProperty) : this(productProperty.id)
}