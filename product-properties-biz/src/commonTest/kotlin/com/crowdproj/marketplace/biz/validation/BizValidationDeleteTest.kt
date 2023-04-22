package com.crowdproj.marketplace.biz.validation

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.models.PropCommand
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationDeleteTest {
    private val command = PropCommand.DELETE
    private val processor = ProductPropertyProcessor()

    @Test
    fun correctId() = validationIdCorrect(command, processor)

    @Test
    fun trimId() = validationIdTrim(command, processor)

    @Test
    fun emptyId() = validationIdEmpty(command, processor)

    @Test
    fun badFormatId() = validationIdFormat(command, processor)
}