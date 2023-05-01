package com.crowdproj.marketplace.repository.inmemory

import com.crowdproj.marketplace.repository.tests.RepoPropDeleteTest

class PropRepoInMemoryDeleteTest : RepoPropDeleteTest() {
    override val repo = PropRepoInMemory(
        initObjects = initObjects,
    )
}