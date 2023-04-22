package com.crowdproj.marketplace.biz.stub

import PropStub
import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.stubs.PropStubs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProductPropertyReadStubTest {
    private val processor = ProductPropertyProcessor()

    @Test
    fun read() = runTest {
        val ctx = setUpContext(PropStubs.SUCCESS)
        processor.exec(ctx)
        val stubList = PropStub.getList()
        assertEquals(stubList, ctx.propertiesResponse)
        assertTrue { ctx.errors.isEmpty() }
    }

    @Test
    fun badId() = runTest {
        val ctx = setUpContext(PropStubs.BAD_ID)
        processor.exec(ctx)
        assertEquals(ProductProperty(), ctx.propertyResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = setUpContext(PropStubs.DB_ERROR)
        processor.exec(ctx)
        assertEquals(ProductProperty(), ctx.propertyResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = setUpContext(PropStubs.NOT_FOUND)
        processor.exec(ctx)
        assertEquals(ProductProperty(), ctx.propertyResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    private fun setUpContext(stubCase: PropStubs) = PropContext(
        state = PropState.NONE,
        command = PropCommand.READ,
        workMode = PropWorkMode.STUB,
        stubCase = stubCase,
        propertiesRequest = mutableListOf(
            ProductProperty(
                id = ProductPropertyId("1")
            ),
            ProductProperty(
                id = ProductPropertyId("2")
            ),
            ProductProperty(
                id = ProductPropertyId("3")
            )
        )
    )
}