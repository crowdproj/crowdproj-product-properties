package com.crowdproj.marketplace.common.repo

import com.crowdproj.marketplace.common.models.ProductPropertyId

data class ProductPropertiesIdsRequest(
    val ids: List<ProductPropertyId>
)