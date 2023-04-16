package com.crowdproj.marketplace.biz.validation

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.ProductPropertyFilter
import com.crowdproj.marketplace.common.models.PropCommand
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.models.PropWorkMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationSearchTest {

    private val command = PropCommand.SEARCH
    private val processor = ProductPropertyProcessor()

    @Test
    fun correctEmpty() = runTest {
        val ctx = PropContext(
            command = command,
            state = PropState.NONE,
            workMode = PropWorkMode.TEST,
            propertiesFilterRequest = ProductPropertyFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(PropState.FAILING, ctx.state)
    }
}