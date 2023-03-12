package com.crowdproj.marketplace.api.v1.request


import com.crowdproj.marketplace.api.v1.models.IProductPropertyRequest
import com.crowdproj.marketplace.api.v1.models.ProductPropertyReadRequest
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass


object ReadRequestStrategy : IRequestStrategy {
    override val discriminator: String = "read"
    override val clazz: KClass<out IProductPropertyRequest> = ProductPropertyReadRequest::class
    override val serializer: KSerializer<out IProductPropertyRequest> = ProductPropertyReadRequest.serializer()
    override fun <T : IProductPropertyRequest> fillDiscriminator(req: T): T {
        require(req is ProductPropertyReadRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}