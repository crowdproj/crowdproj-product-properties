package com.crowdproj.marketplace.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import com.crowdproj.marketplace.logging.common.IPropLogWrapper
import kotlin.reflect.KClass

@Suppress("unused")
fun propLoggerKermit(loggerId: String): IPropLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return PropLoggerWrapperKermit(
        logger = logger,
        loggerId = loggerId,
    )
}

@Suppress("unused")
fun propLoggerKermit(cls: KClass<*>): IPropLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return PropLoggerWrapperKermit(
        logger = logger,
        loggerId = cls.qualifiedName ?: "",
    )
}