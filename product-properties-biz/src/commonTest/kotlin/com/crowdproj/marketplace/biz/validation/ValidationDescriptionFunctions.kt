package com.crowdproj.marketplace.biz.validation

import PropStub
import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.PropCommand
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.models.PropWorkMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = PropStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionCorrect(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = "abc",
            description = "abc",
            unitMain = stub.unitMain,
            units = stub.units
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(PropState.FAILING, ctx.state)
    assertEquals("abc", ctx.propValidated.description)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionTrim(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = stub.name,
            description = " \n \t abc \t\n   ",
            unitMain = stub.unitMain,
            units = stub.units
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(PropState.FAILING, ctx.state)
    assertEquals("abc", ctx.propValidated.description)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionEmpty(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = stub.name,
            description = "",
            unitMain = stub.unitMain,
            units = stub.units
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(PropState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
    assertEquals("validation-description-empty", error?.code)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionSymbols(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = "abc",
            description = "!@#\$%^&*(),.{}",
            unitMain = stub.unitMain,
            units = stub.units
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(PropState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
    assertEquals("validation-description-noContent", error?.code)
}