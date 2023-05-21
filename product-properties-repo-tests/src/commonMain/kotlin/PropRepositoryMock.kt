package com.crowdproj.marketplace.repository.tests

import com.crowdproj.marketplace.common.repo.*

class PropRepositoryMock(
    private val invokeCreateProp: (ProductPropertyRequest) -> ProductPropertyResponse = { ProductPropertyResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadProp: (ProductPropertiesIdsRequest) -> ProductPropertiesResponse = { ProductPropertiesResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateProp: (ProductPropertyRequest) -> ProductPropertyResponse = { ProductPropertyResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteProp: (ProductPropertyIdRequest) -> ProductPropertyResponse = { ProductPropertyResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchProp: (ProductPropertyFilterRequest) -> ProductPropertiesResponse = { ProductPropertiesResponse.MOCK_SUCCESS_EMPTY },
) : IPropRepository {
    override suspend fun createProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse =
        invokeCreateProp(rq)

    override suspend fun readProductProperties(rq: ProductPropertiesIdsRequest): ProductPropertiesResponse =
        invokeReadProp(rq)

    override suspend fun updateProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse =
        invokeUpdateProp(rq)

    override suspend fun deleteProductProperty(rq: ProductPropertyIdRequest): ProductPropertyResponse =
        invokeDeleteProp(rq)

    override suspend fun searchProductProperty(rq: ProductPropertyFilterRequest): ProductPropertiesResponse =
        invokeSearchProp(rq)
}