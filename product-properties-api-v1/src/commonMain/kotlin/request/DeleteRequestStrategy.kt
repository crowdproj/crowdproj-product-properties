package com.crowdproj.marketplace.api.v1.request

import com.crowdproj.marketplace.api.v1.models.IProductPropertyRequest
import com.crowdproj.marketplace.api.v1.models.ProductPropertyDeleteRequest
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

object DeleteRequestStrategy : IRequestStrategy {
    override val discriminator: String = "delete"
    override val clazz: KClass<out IProductPropertyRequest> = ProductPropertyDeleteRequest::class
    override val serializer: KSerializer<out IProductPropertyRequest> = ProductPropertyDeleteRequest.serializer()
    override fun <T : IProductPropertyRequest> fillDiscriminator(req: T): T {
        require(req is ProductPropertyDeleteRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}