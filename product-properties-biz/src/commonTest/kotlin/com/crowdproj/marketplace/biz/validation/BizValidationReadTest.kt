package com.crowdproj.marketplace.biz.validation

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.common.models.PropCommand
import com.crowdproj.marketplace.repository.stubs.PropRepoStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationReadTest {
    private val command = PropCommand.READ
    private val settings by lazy {
        PropCorSettings(
            repoTest = PropRepoStub()
        )
    }
    private val processor = ProductPropertyProcessor(settings)

    @Test
    fun correctIds() = validationIdsCorrect(command, processor)

    @Test
    fun trimIds() = validationIdsTrim(command, processor)

    @Test
    fun emptyIds() = validationIdsEmpty(command, processor)

    @Test
    fun badFormatIds() = validationIdsFormat(command, processor)
}