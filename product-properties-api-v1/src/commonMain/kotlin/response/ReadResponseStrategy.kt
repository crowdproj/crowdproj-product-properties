package com.crowdproj.marketplace.api.v1.response

import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse
import com.crowdproj.marketplace.api.v1.models.ProductPropertyReadResponse
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

object ReadResponseStrategy : IResponseStrategy {
    override val discriminator: String = "read"
    override val clazz: KClass<out IProductPropertyResponse> = ProductPropertyReadResponse::class
    override val serializer: KSerializer<out IProductPropertyResponse> = ProductPropertyReadResponse.serializer()
    override fun <T : IProductPropertyResponse> fillDiscriminator(req: T): T {
        require(req is ProductPropertyReadResponse)
        @Suppress("UNCHECKED_CAST")
        return req.copy(responseType = discriminator) as T
    }
}