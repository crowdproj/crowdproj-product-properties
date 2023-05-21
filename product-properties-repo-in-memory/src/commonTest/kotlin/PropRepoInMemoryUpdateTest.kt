package com.crowdproj.marketplace.repository.inmemory

import com.crowdproj.marketplace.repository.tests.RepoPropUpdateTest

class PropRepoInMemoryUpdateTest : RepoPropUpdateTest() {
    override val repo = PropRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}