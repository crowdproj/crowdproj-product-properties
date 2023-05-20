package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.app.configs.ConfigPaths
import com.crowdproj.marketplace.app.configs.GremlinConfig
import com.crowdproj.marketplace.common.repo.IPropRepository
import com.crowdproj.marketplace.repository.gremlin.PropRepoGremlin
import com.crowdproj.marketplace.repository.inmemory.PropRepoInMemory
import io.ktor.server.application.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

actual fun Application.getDatabaseConf(type: PropDbType): IPropRepository {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "arcade", "arcadedb", "graphdb", "gremlin", "g", "a" -> initGremliln()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'gremlin'"
        )
    }
}

private fun Application.initGremliln(): IPropRepository {
    val config = GremlinConfig(environment.config)
    return PropRepoGremlin(
        hosts = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
        enableSsl = config.enableSsl,
    )
}

private fun Application.initInMemory(): IPropRepository {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return PropRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}