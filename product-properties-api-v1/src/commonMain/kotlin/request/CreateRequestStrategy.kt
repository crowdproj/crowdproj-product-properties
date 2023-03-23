package com.crowdproj.marketplace.api.v1.request

import com.crowdproj.marketplace.api.v1.models.IProductPropertyRequest
import com.crowdproj.marketplace.api.v1.models.ProductPropertyCreateRequest
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass
object CreateRequestStrategy : IRequestStrategy {
    override val discriminator: String = "create"
    override val clazz: KClass<out IProductPropertyRequest> = ProductPropertyCreateRequest::class
    override val serializer: KSerializer<out IProductPropertyRequest> = ProductPropertyCreateRequest.serializer()
    override fun <T : IProductPropertyRequest> fillDiscriminator(req: T): T {
        require(req is ProductPropertyCreateRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}