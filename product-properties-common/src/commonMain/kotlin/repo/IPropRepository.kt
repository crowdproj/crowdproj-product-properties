package com.crowdproj.marketplace.common.repo

interface IPropRepository {
    suspend fun createProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse

    suspend fun readProductProperties(rq: ProductPropertiesIdsRequest): ProductPropertiesResponse

    suspend fun updateProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse

    suspend fun deleteProductProperty(rq: ProductPropertyIdRequest): ProductPropertyResponse

    suspend fun searchProductProperty(rq: ProductPropertyFilterRequest): ProductPropertiesResponse

    companion object {
        val NONE = object : IPropRepository {
            override suspend fun createProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readProductProperties(rq: ProductPropertiesIdsRequest): ProductPropertiesResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteProductProperty(rq: ProductPropertyIdRequest): ProductPropertyResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchProductProperty(rq: ProductPropertyFilterRequest): ProductPropertiesResponse {
                TODO("Not yet implemented")
            }
        }
    }
}