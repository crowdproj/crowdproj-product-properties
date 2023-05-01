package com.crowdproj.marketplace.repository.stubs

import com.crowdproj.marketplace.common.repo.*
import com.crowdproj.marketplace.stubs.PropStub

class PropRepoStub() : IPropRepository {
    override fun createProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
        return ProductPropertyResponse(
            data = PropStub.prepareResult { },
            isSuccess = true,
        )
    }

    override fun readProductProperties(rq: ProductPropertiesIdsRequest): ProductPropertiesResponse {
        return ProductPropertiesResponse(
            data = PropStub.getList(),
            isSuccess = true,
        )
    }

    override fun updateProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
        return ProductPropertyResponse(
            data = PropStub.prepareResult { },
            isSuccess = true,
        )
    }

    override fun deleteProductProperty(rq: ProductPropertyIdRequest): ProductPropertyResponse {
        return ProductPropertyResponse(
            data = PropStub.getDeleted(),
            isSuccess = true,
        )
    }

    override fun searchProductProperty(rq: ProductPropertyFilterRequest): ProductPropertiesResponse {
        return ProductPropertiesResponse(
            data = PropStub.getList(),
            isSuccess = true,
        )
    }
}