package com.crowdproj.marketplace.logging.common

import kotlin.reflect.KClass

class PropLoggerProvider(
    private val provider: (String) -> IPropLogWrapper = { IPropLogWrapper.DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")
}