package com.crowdproj.marketplace.repository.gremlin

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.repository.tests.RepoPropReadTest

class PropRepoGremlinReadTest : RepoPropReadTest() {
    override val repo: PropRepoGremlin by lazy {
        PropRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            user = ArcadeDbContainer.username,
            pass = ArcadeDbContainer.password,
            enableSsl = false,
            initObjects = initObjects,
            initRepo = { g -> g.V().drop().iterate() },
        )
    }
    override val readSuccess: List<ProductProperty> by lazy { repo.initializedObjects }
}
