package com.crowdproj.marketplace.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class PropLoggerProvider(
    private val provider: (String) -> IPropLogWrapper = { IPropLogWrapper.DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    fun logger(function: KFunction<*>) = provider(function.name)
}