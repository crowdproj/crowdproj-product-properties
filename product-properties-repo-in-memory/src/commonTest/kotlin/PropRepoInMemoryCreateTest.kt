package com.crowdproj.marketplace.repository.inmemory

import com.crowdproj.marketplace.repository.tests.RepoPropCreateTest

class PropRepoInMemoryCreateTest : RepoPropCreateTest() {
    override val repo = PropRepoInMemory(
        initObjects = initObjects,
    )
}