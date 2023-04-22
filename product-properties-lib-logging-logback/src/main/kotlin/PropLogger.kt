package com.crowdproj.marketplace.logging.jvm

import ch.qos.logback.classic.Logger
import com.crowdproj.marketplace.logging.common.IPropLogWrapper
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun propLoggerLogback(logger: Logger): IPropLogWrapper = PropLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun propLoggerLogback(clazz: KClass<*>): IPropLogWrapper =
    propLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)

@Suppress("unused")
fun propLoggerLogback(loggerId: String): IPropLogWrapper =
    propLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)