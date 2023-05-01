package com.crowdproj.marketplace.common.repo

interface IPropRepository {
    fun createProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse

    fun readProductProperties(rq: ProductPropertiesIdsRequest): ProductPropertiesResponse

    fun updateProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse

    fun deleteProductProperty(rq: ProductPropertyIdRequest): ProductPropertyResponse

    fun searchProductProperty(rq: ProductPropertyFilterRequest): ProductPropertiesResponse

    companion object {
        val NONE = object : IPropRepository {
            override fun createProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
                TODO("Not yet implemented")
            }

            override fun readProductProperties(rq: ProductPropertiesIdsRequest): ProductPropertiesResponse {
                TODO("Not yet implemented")
            }

            override fun updateProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
                TODO("Not yet implemented")
            }

            override fun deleteProductProperty(rq: ProductPropertyIdRequest): ProductPropertyResponse {
                TODO("Not yet implemented")
            }

            override fun searchProductProperty(rq: ProductPropertyFilterRequest): ProductPropertiesResponse {
                TODO("Not yet implemented")
            }
        }
    }
}