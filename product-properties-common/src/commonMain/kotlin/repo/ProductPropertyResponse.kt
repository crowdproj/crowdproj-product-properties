package com.crowdproj.marketplace.common.repo

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.PropError

class ProductPropertyResponse(
    override val data: ProductProperty?,
    override val isSuccess: Boolean,
    override val errors: List<PropError> = emptyList()
) : IDbResponse<ProductProperty> {

    companion object {
        val MOCK_SUCCESS_EMPTY = ProductPropertyResponse(null, true)
        fun success(result: ProductProperty) = ProductPropertyResponse(result, true)
        fun error(errors: List<PropError>) = ProductPropertyResponse(null, false, errors)
        fun error(error: PropError) = ProductPropertyResponse(null, false, listOf(error))
    }
}