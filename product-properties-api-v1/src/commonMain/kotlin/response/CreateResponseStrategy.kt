package com.crowdproj.marketplace.api.v1.response


import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse
import com.crowdproj.marketplace.api.v1.models.ProductPropertyCreateResponse
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

object CreateResponseStrategy : IResponseStrategy {
    override val discriminator: String = "create"
    override val clazz: KClass<out IProductPropertyResponse> = ProductPropertyCreateResponse::class
    override val serializer: KSerializer<out IProductPropertyResponse> = ProductPropertyCreateResponse.serializer()
    override fun <T : IProductPropertyResponse> fillDiscriminator(req: T): T {
        require(req is ProductPropertyCreateResponse)
        @Suppress("UNCHECKED_CAST")
        return req.copy(responseType = discriminator) as T
    }
}