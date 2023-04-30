package com.crowdproj.marketplace.biz.stub

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.stubs.PropStubs
import com.crowdproj.marketplace.stubs.PropStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProductPropertyDeleteStubTest {
    private val processor = ProductPropertyProcessor()

    @Test
    fun delete() = runTest {
        val ctx = setUpContext(PropStubs.SUCCESS)

        processor.exec(ctx)
        val stub = PropStub.getDeleted()
        assertEquals(stub.id, ctx.propertyResponse.id)
        assertEquals(stub.name, ctx.propertyResponse.name)
        assertEquals(stub.description, ctx.propertyResponse.description)
        assertEquals(stub.unitMain, ctx.propertyResponse.unitMain)
        assertEquals(stub.units, ctx.propertyResponse.units)
        assertEquals(true, ctx.propertyResponse.deleted)
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
        command = PropCommand.DELETE,
        workMode = PropWorkMode.STUB,
        stubCase = stubCase,
        propertyRequest = ProductProperty(
            id = ProductPropertyId("100_000")
        ),
    )
}