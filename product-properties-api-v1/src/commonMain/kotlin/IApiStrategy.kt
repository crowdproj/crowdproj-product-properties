package com.crowdproj.marketplace.api.v1

import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

interface IApiStrategy<K : Any> {
    val discriminator: String
    val clazz: KClass<out K>
    val serializer: KSerializer<out K>
    fun <T : K> fillDiscriminator(req: T): T
}