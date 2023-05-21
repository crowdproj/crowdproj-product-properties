package com.crowdproj.marketplace.common.repo

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.PropError

class ProductPropertiesResponse(
    override val data: List<ProductProperty>?,
    override val isSuccess: Boolean,
    override val errors: List<PropError> = emptyList(),
) : IDbResponse<List<ProductProperty>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = ProductPropertiesResponse(emptyList(), true)
        fun success(result: List<ProductProperty>) = ProductPropertiesResponse(result, true)
        fun error(errors: List<PropError>) = ProductPropertiesResponse(null, false, errors)
        fun error(error: PropError) = ProductPropertiesResponse(null, false, listOf(error))
    }
}