package com.crowdproj.marketplace.biz.validation

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.models.PropCommand
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationReadTest {
    private val command = PropCommand.READ
    private val processor = ProductPropertyProcessor()

    @Test
    fun correctIds() = validationIdsCorrect(command, processor)

    @Test
    fun trimIds() = validationIdsTrim(command, processor)

    @Test
    fun emptyIds() = validationIdsEmpty(command, processor)

    @Test
    fun badFormatIds() = validationIdsFormat(command, processor)
}