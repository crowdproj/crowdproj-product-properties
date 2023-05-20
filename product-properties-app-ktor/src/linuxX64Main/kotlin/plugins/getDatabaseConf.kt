package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.common.repo.IPropRepository
import com.crowdproj.marketplace.repository.inmemory.PropRepoInMemory
import io.ktor.server.application.*

actual fun Application.getDatabaseConf(type: PropDbType): IPropRepository {
    return PropRepoInMemory()
}