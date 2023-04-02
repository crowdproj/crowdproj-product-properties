package com.crowdproj.marketplace.api.v1

import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse
import com.crowdproj.marketplace.api.v1.response.IResponseStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

val ProductPropertyResponseSerializer = ResponseSerializer(ProductPropertyResponseSerializerBase)

private object ProductPropertyResponseSerializerBase :
    JsonContentPolymorphicSerializer<IProductPropertyResponse>(IProductPropertyResponse::class) {
    private const val discriminator = "responseType"

    override fun selectDeserializer(element: JsonElement): KSerializer<out IProductPropertyResponse> {

        val discriminatorValue = element.jsonObject[discriminator]?.jsonPrimitive?.content
        return IResponseStrategy.membersByDiscriminator[discriminatorValue]?.serializer
            ?: throw SerializationException(
                "Unknown value '${discriminatorValue}' in discriminator '$discriminator' " +
                        "property of ${IProductPropertyResponse::class} implementation"
            )
    }
}

class ResponseSerializer<T : IProductPropertyResponse>(private val serializer: KSerializer<T>) :
    KSerializer<T> by serializer {
    override fun serialize(encoder: Encoder, value: T) =
        IResponseStrategy
            .membersByClazz[value::class]
            ?.fillDiscriminator(value)
            ?.let { serializer.serialize(encoder, it) }
            ?: throw SerializationException(
                "Unknown class to serialize as IResponse instance in ResponseSerializer"
            )
}