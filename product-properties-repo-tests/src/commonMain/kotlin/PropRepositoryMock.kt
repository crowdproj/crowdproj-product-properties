package com.crowdproj.marketplace.repository.tests

import com.crowdproj.marketplace.common.repo.*

class PropRepositoryMock(
    private val invokeCreateProp: (ProductPropertyRequest) -> ProductPropertyResponse = { ProductPropertyResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadProp: (ProductPropertiesIdsRequest) -> ProductPropertiesResponse = { ProductPropertiesResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateProp: (ProductPropertyRequest) -> ProductPropertyResponse = { ProductPropertyResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteProp: (ProductPropertyIdRequest) -> ProductPropertyResponse = { ProductPropertyResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchProp: (ProductPropertyFilterRequest) -> ProductPropertiesResponse = { ProductPropertiesResponse.MOCK_SUCCESS_EMPTY },
) : IPropRepository {
    override fun createProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse =
        invokeCreateProp(rq)

    override fun readProductProperties(rq: ProductPropertiesIdsRequest): ProductPropertiesResponse =
        invokeReadProp(rq)

    override fun updateProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse =
        invokeUpdateProp(rq)

    override fun deleteProductProperty(rq: ProductPropertyIdRequest): ProductPropertyResponse =
        invokeDeleteProp(rq)

    override fun searchProductProperty(rq: ProductPropertyFilterRequest): ProductPropertiesResponse =
        invokeSearchProp(rq)
}