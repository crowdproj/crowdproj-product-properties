package com.crowdproj.marketplace.api.v1.response

import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse
import com.crowdproj.marketplace.api.v1.models.ProductPropertyUpdateResponse
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

object UpdateResponseStrategy : IResponseStrategy {
    override val discriminator: String = "update"
    override val clazz: KClass<out IProductPropertyResponse> = ProductPropertyUpdateResponse::class
    override val serializer: KSerializer<out IProductPropertyResponse> = ProductPropertyUpdateResponse.serializer()
    override fun <T : IProductPropertyResponse> fillDiscriminator(req: T): T {
        require(req is ProductPropertyUpdateResponse)
        @Suppress("UNCHECKED_CAST")
        return req.copy(responseType = discriminator) as T
    }
}