package com.crowdproj.marketplace.api.v1.request

import com.crowdproj.marketplace.api.v1.models.IProductPropertyRequest
import com.crowdproj.marketplace.api.v1.models.ProductPropertyUpdateRequest
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass


object UpdateRequestStrategy : IRequestStrategy {
    override val discriminator: String = "update"
    override val clazz: KClass<out IProductPropertyRequest> = ProductPropertyUpdateRequest::class
    override val serializer: KSerializer<out IProductPropertyRequest> = ProductPropertyUpdateRequest.serializer()
    override fun <T : IProductPropertyRequest> fillDiscriminator(req: T): T {
        require(req is ProductPropertyUpdateRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}