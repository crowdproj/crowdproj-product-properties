package com.crowdproj.marketplace.repository.gremlin

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.repository.tests.RepoPropSearchTest

class PropRepoGremlinSearchTest : RepoPropSearchTest() {
    override val repo: PropRepoGremlin by lazy {
        PropRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            user = ArcadeDbContainer.username,
            pass = ArcadeDbContainer.password,
            initObjects = initObjects,
            initRepo = { g -> g.V().drop().iterate() },
        )
    }

    override val initializedObjects: List<ProductProperty> by lazy {
        repo.initializedObjects
    }
}