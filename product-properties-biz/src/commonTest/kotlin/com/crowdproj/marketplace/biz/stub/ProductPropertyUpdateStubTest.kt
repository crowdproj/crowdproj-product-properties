package com.crowdproj.marketplace.biz.stub

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.stubs.PropStubs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProductPropertyUpdateStubTest {

    private val processor = ProductPropertyProcessor()

    private val id = ProductPropertyId("100_000")
    private val name = "name"
    private val description = "desc"
    private val unitMain = ProductUnitId("100")
    private val units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))

    @Test
    fun update() = runTest {
        val ctx = setUpContext(PropStubs.SUCCESS)

        processor.exec(ctx)
        assertEquals(id, ctx.propertyResponse.id)
        assertEquals(name, ctx.propertyResponse.name)
        assertEquals(description, ctx.propertyResponse.description)
        assertEquals(unitMain, ctx.propertyResponse.unitMain)
        assertEquals(units, ctx.propertyResponse.units)
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
    fun badName() = runTest {
        val ctx = setUpContext(PropStubs.BAD_NAME)
        processor.exec(ctx)
        assertEquals(ProductProperty(), ctx.propertyResponse)
        assertEquals("name", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badDescription() = runTest {
        val ctx = setUpContext(PropStubs.BAD_DESCRIPTION)
        processor.exec(ctx)
        assertEquals(ProductProperty(), ctx.propertyResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
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
        command = PropCommand.UPDATE,
        workMode = PropWorkMode.STUB,
        stubCase = stubCase,
        propertyRequest = ProductProperty(
            id = id,
            name = name,
            description = description,
            unitMain = unitMain,
            units = units
        ),
    )
}