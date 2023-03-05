package com.crowdproj.marketplace.api.v1

import com.crowdproj.marketplace.api.v1.models.*
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
            @Suppress("UNCHECKED_CAST")
            when (it) {
                is ProductPropertyCreateRequest -> RequestSerializer(ProductPropertyCreateRequest.serializer()) as SerializationStrategy<IProductPropertyRequest>
                is ProductPropertyReadRequest -> RequestSerializer(ProductPropertyReadRequest.serializer()) as SerializationStrategy<IProductPropertyRequest>
                is ProductPropertyUpdateRequest -> RequestSerializer(ProductPropertyUpdateRequest.serializer()) as SerializationStrategy<IProductPropertyRequest>
                is ProductPropertyDeleteRequest -> RequestSerializer(ProductPropertyDeleteRequest.serializer()) as SerializationStrategy<IProductPropertyRequest>
                is ProductPropertySearchRequest -> RequestSerializer(ProductPropertySearchRequest.serializer()) as SerializationStrategy<IProductPropertyRequest>
                else -> null
            }
        }
        polymorphicDefaultSerializer(IProductPropertyResponse::class) {
            @Suppress("UNCHECKED_CAST")
            when (it) {
                is ProductPropertyCreateResponse -> ResponseSerializer(ProductPropertyCreateResponse.serializer()) as SerializationStrategy<IProductPropertyResponse>
                is ProductPropertyReadResponse -> ResponseSerializer(ProductPropertyReadResponse.serializer()) as SerializationStrategy<IProductPropertyResponse>
                is ProductPropertyUpdateResponse -> ResponseSerializer(ProductPropertyUpdateResponse.serializer()) as SerializationStrategy<IProductPropertyResponse>
                is ProductPropertyDeleteResponse -> ResponseSerializer(ProductPropertyDeleteResponse.serializer()) as SerializationStrategy<IProductPropertyResponse>
                is ProductPropertySearchResponse -> ResponseSerializer(ProductPropertySearchResponse.serializer()) as SerializationStrategy<IProductPropertyResponse>
                else -> null
            }
        }
        contextual(ProductPropertyRequestSerializer)
        contextual(ProductPropertyResponseSerializer)
    }
}