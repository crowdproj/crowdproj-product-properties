package com.crowdproj.marketplace.repository.gremlin

import com.crowdproj.marketplace.common.repo.IPropRepository
import com.crowdproj.marketplace.repository.tests.RepoPropCreateTest
import com.crowdproj.marketplace.repository.tests.RepoPropSearchTest

class PropRepoGremlinCreateTest : RepoPropCreateTest() {
    override val repo: IPropRepository by lazy {
        PropRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            user = ArcadeDbContainer.username,
            pass = ArcadeDbContainer.password,
            initObjects = RepoPropSearchTest.initObjects,
            initRepo = { g -> g.V().drop().iterate() },
            randomUuid = { lockNew.asString() }
        )
    }
}