package com.crowdproj.marketplace.repository.stubs

import com.crowdproj.marketplace.common.repo.*
import com.crowdproj.marketplace.stubs.PropStub

class PropRepoStub() : IPropRepository {
    override suspend fun createProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
        return ProductPropertyResponse(
            data = PropStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun readProductProperties(rq: ProductPropertiesIdsRequest): ProductPropertiesResponse {
        return ProductPropertiesResponse(
            data = PropStub.getList(),
            isSuccess = true,
        )
    }

    override suspend fun updateProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
        return ProductPropertyResponse(
            data = PropStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun deleteProductProperty(rq: ProductPropertyIdRequest): ProductPropertyResponse {
        return ProductPropertyResponse(
            data = PropStub.getDeleted(),
            isSuccess = true,
        )
    }

    override suspend fun searchProductProperty(rq: ProductPropertyFilterRequest): ProductPropertiesResponse {
        return ProductPropertiesResponse(
            data = PropStub.getList(),
            isSuccess = true,
        )
    }
}