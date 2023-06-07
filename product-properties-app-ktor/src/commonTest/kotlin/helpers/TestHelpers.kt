package com.crowdproj.marketplace.app.helpers

import com.crowdproj.marketplace.app.PropAppSettings
import com.crowdproj.marketplace.app.base.KtorAuthConfig
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.common.repo.IPropRepository
import com.crowdproj.marketplace.repository.inmemory.PropRepoInMemory
import com.crowdproj.marketplace.repository.stubs.PropRepoStub

fun testSettings(repo: IPropRepository? = null) = PropAppSettings(
    corSettings = PropCorSettings(
        repoStub = PropRepoStub(),
        repoTest = repo ?: PropRepoInMemory(),
        repoProd = repo ?: PropRepoInMemory(),
    ),
    auth = KtorAuthConfig.TEST
)
