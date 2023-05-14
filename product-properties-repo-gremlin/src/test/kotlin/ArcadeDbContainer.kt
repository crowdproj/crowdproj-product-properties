package com.crowdproj.marketplace.repository.gremlin

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.ARCADEDB_VERSION
import java.time.Duration

object ArcadeDbContainer {
    val username: String = "root"
    val password: String = "root_root"
    val container by lazy {
        GenericContainer(DockerImageName.parse("arcadedata/arcadedb:${ARCADEDB_VERSION}")).apply {
            withExposedPorts(2480, 2424, 8182)
            withEnv(
                "JAVA_OPTS", "-Darcadedb.server.rootPassword=$password " +
                        "-Darcadedb.server.plugins=GremlinServer:com.arcadedb.server.gremlin.GremlinServerPlugin"
            )
            waitingFor(Wait.forLogMessage(".*ArcadeDB Server started.*\\n", 1))
            withStartupTimeout(Duration.ofMinutes(5))
            start()
            println("ARCADE: http://${host}:${getMappedPort(2480)}")
            println("ARCADE: http://${host}:${getMappedPort(2424)}")
            println(this.logs)
            println("RUNNING?: ${this.isRunning}")
        }
    }
}
