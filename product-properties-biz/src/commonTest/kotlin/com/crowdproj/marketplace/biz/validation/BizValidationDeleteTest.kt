package com.crowdproj.marketplace.biz.validation

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.common.models.PropCommand
import com.crowdproj.marketplace.repository.stubs.PropRepoStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationDeleteTest {
    private val command = PropCommand.DELETE
    private val settings by lazy {
        PropCorSettings(
            repoTest = PropRepoStub()
        )
    }
    private val processor = ProductPropertyProcessor(settings)

    @Test
    fun correctId() = validationIdCorrect(command, processor)

    @Test
    fun trimId() = validationIdTrim(command, processor)

    @Test
    fun emptyId() = validationIdEmpty(command, processor)

    @Test
    fun badFormatId() = validationIdFormat(command, processor)
}