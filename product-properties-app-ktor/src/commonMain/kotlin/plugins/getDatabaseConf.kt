package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.common.repo.IPropRepository
import io.ktor.server.application.*

expect fun Application.getDatabaseConf(type: PropDbType): IPropRepository

enum class PropDbType(val confName: String) {
    PROD("prod"), TEST("test")
}