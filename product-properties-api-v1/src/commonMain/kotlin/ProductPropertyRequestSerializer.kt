package com.crowdproj.marketplace.api.v1

import com.crowdproj.marketplace.api.v1.models.IProductPropertyRequest
import com.crowdproj.marketplace.api.v1.request.IRequestStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


val ProductPropertyRequestSerializer = RequestSerializer(ProductPropertyRequestSerializerBase)

private object ProductPropertyRequestSerializerBase :
    JsonContentPolymorphicSerializer<IProductPropertyRequest>(IProductPropertyRequest::class) {
    private const val discriminator = "requestType"

    override fun selectDeserializer(element: JsonElement): KSerializer<out IProductPropertyRequest> {

        val discriminatorValue = element.jsonObject[discriminator]?.jsonPrimitive?.content
        return IRequestStrategy.membersByDiscriminator[discriminatorValue]?.serializer
            ?: throw SerializationException(
                "Unknown value '${discriminatorValue}' in discriminator '$discriminator' " +
                        "property of ${IProductPropertyRequest::class} implementation"
            )
    }
}

class RequestSerializer<T : IProductPropertyRequest>(private val serializer: KSerializer<T>) :
    KSerializer<T> by serializer {
    override fun serialize(encoder: Encoder, value: T) =
        IRequestStrategy
            .membersByClazz[value::class]
            ?.fillDiscriminator(value)
            ?.let { serializer.serialize(encoder, it) }
            ?: throw SerializationException(
                "Unknown class to serialize as IRequest instance in RequestSerializer"
            )
}
