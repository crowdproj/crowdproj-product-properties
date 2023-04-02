package com.crowdproj.marketplace.api.v1

import com.crowdproj.marketplace.api.v1.models.*
import com.crowdproj.marketplace.api.v1.request.IRequestStrategy
import com.crowdproj.marketplace.api.v1.response.IResponseStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

@OptIn(ExperimentalSerializationApi::class)
val apiV1Mapper = Json {
    classDiscriminator = "_"
    encodeDefaults = true
    ignoreUnknownKeys = true

    serializersModule = SerializersModule {
        polymorphicDefaultSerializer(IProductPropertyRequest::class) {
            val strategy = IRequestStrategy.membersByClazz[it::class] ?: return@polymorphicDefaultSerializer null
            @Suppress("UNCHECKED_CAST")
            RequestSerializer(strategy.serializer) as SerializationStrategy<IProductPropertyRequest>
        }
        polymorphicDefault(IProductPropertyRequest::class) {
            ProductPropertyRequestSerializer
        }
        polymorphicDefaultSerializer(IProductPropertyResponse::class) {
            val strategy = IResponseStrategy.membersByClazz[it::class] ?: return@polymorphicDefaultSerializer null
            @Suppress("UNCHECKED_CAST")
            ResponseSerializer(strategy.serializer) as SerializationStrategy<IProductPropertyResponse>
        }
        polymorphicDefault(IProductPropertyResponse::class) {
            ProductPropertyResponseSerializer
        }
        contextual(ProductPropertyRequestSerializer)
        contextual(ProductPropertyResponseSerializer)
    }
}

fun Json.encodeResponse(response: IProductPropertyResponse): String =
    encodeToString(ProductPropertyResponseSerializer, response)