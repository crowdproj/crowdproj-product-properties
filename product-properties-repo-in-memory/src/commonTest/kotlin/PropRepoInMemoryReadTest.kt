package com.crowdproj.marketplace.repository.inmemory

import com.crowdproj.marketplace.repository.tests.RepoPropReadTest

class PropRepoInMemoryReadTest : RepoPropReadTest() {
    override val repo = PropRepoInMemory(
        initObjects = initObjects,
    )
}