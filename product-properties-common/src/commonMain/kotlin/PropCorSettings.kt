package com.crowdproj.marketplace.common

import com.crowdproj.marketplace.logging.common.PropLoggerProvider

data class PropCorSettings(
    val loggerProvider: PropLoggerProvider = PropLoggerProvider(),
) {
    companion object {
        val NONE = PropCorSettings()
    }
}