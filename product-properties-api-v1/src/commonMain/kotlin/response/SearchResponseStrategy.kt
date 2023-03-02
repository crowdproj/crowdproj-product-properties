package com.crowdproj.marketplace.api.v1.response

import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse
import com.crowdproj.marketplace.api.v1.models.ProductPropertySearchResponse
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

object SearchResponseStrategy : IResponseStrategy {
    override val discriminator: String = "search"
    override val clazz: KClass<out IProductPropertyResponse> = ProductPropertySearchResponse::class
    override val serializer: KSerializer<out IProductPropertyResponse> = ProductPropertySearchResponse.serializer()
    override fun <T : IProductPropertyResponse> fillDiscriminator(req: T): T {
        require(req is ProductPropertySearchResponse)
        @Suppress("UNCHECKED_CAST")
        return req.copy(responseType = discriminator) as T
    }
}