package com.crowdproj.marketplace.repository.inmemory

import com.crowdproj.marketplace.repository.tests.RepoPropSearchTest

class PropRepoInMemorySearchTest : RepoPropSearchTest() {
    override val repo = PropRepoInMemory(
        initObjects = initObjects,
    )
}