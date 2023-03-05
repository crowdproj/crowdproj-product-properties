package com.crowdproj.marketplace.api.v1.response

import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse
import com.crowdproj.marketplace.api.v1.models.ProductPropertyDeleteResponse
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass


object DeleteResponseStrategy : IResponseStrategy {
    override val discriminator: String = "delete"
    override val clazz: KClass<out IProductPropertyResponse> = ProductPropertyDeleteResponse::class
    override val serializer: KSerializer<out IProductPropertyResponse> = ProductPropertyDeleteResponse.serializer()
    override fun <T : IProductPropertyResponse> fillDiscriminator(req: T): T {
        require(req is ProductPropertyDeleteResponse)
        @Suppress("UNCHECKED_CAST")
        return req.copy(responseType = discriminator) as T
    }
}