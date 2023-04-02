package com.crowdproj.marketplace.api.v1.response

import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse
import com.crowdproj.marketplace.api.v1.models.ProductPropertyInitResponse
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

object InitResponseStrategy : IResponseStrategy {
    override val discriminator: String = "init"
    override val clazz: KClass<out IProductPropertyResponse> = ProductPropertyInitResponse::class
    override val serializer: KSerializer<out IProductPropertyResponse> = ProductPropertyInitResponse.serializer()
    override fun <T : IProductPropertyResponse> fillDiscriminator(req: T): T {
        require(req is ProductPropertyInitResponse)
        @Suppress("UNCHECKED_CAST")
        return req.copy(responseType = discriminator) as T
    }
}