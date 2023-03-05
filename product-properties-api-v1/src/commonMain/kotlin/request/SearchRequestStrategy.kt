package com.crowdproj.marketplace.api.v1.request

import com.crowdproj.marketplace.api.v1.models.IProductPropertyRequest
import com.crowdproj.marketplace.api.v1.models.ProductPropertySearchRequest
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

object SearchRequestStrategy : IRequestStrategy {
    override val discriminator: String = "search"
    override val clazz: KClass<out IProductPropertyRequest> = ProductPropertySearchRequest::class
    override val serializer: KSerializer<out IProductPropertyRequest> = ProductPropertySearchRequest.serializer()
    override fun <T : IProductPropertyRequest> fillDiscriminator(req: T): T {
        require(req is ProductPropertySearchRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}