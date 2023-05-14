package com.crowdproj.marketplace.repository.gremlin

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.repository.tests.RepoPropDeleteTest

class PropRepoGremlinDeleteTest : RepoPropDeleteTest() {
    override val repo: PropRepoGremlin by lazy {
        PropRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            user = ArcadeDbContainer.username,
            pass = ArcadeDbContainer.password,
            enableSsl = false,
            initObjects = initObjects,
            initRepo = { g -> g.V().drop().iterate() },
            randomUuid = { lockNew.asString() }
        )
    }
    override val deleteSuccess: ProductProperty by lazy { repo.initializedObjects[0] }
    override val deleteConc: ProductProperty by lazy { repo.initializedObjects[1] }
    override val notFoundId: ProductPropertyId = ProductPropertyId("#3100:0")
}