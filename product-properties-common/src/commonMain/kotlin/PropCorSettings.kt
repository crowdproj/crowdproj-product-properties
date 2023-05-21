package com.crowdproj.marketplace.common

import com.crowdproj.marketplace.common.repo.IPropRepository
import com.crowdproj.marketplace.logging.common.PropLoggerProvider

data class PropCorSettings(
    val loggerProvider: PropLoggerProvider = PropLoggerProvider(),
    val repoStub: IPropRepository = IPropRepository.NONE,
    val repoTest: IPropRepository = IPropRepository.NONE,
    val repoProd: IPropRepository = IPropRepository.NONE,
) {
    companion object {
        val NONE = PropCorSettings()
    }
}