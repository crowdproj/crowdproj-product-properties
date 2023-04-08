package com.crowdproj.marketplace.common.models

class PropError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: Level = Level.ERROR,
    val exception: Throwable? = null,
) {
    @Suppress("unused")
    enum class Level {
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}