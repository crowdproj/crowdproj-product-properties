package com.crowdproj.marketplace.biz.validation

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.stubs.PropStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = PropStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNameCorrect(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = "abc",
            description = stub.description,
            unitMain = stub.unitMain,
            units = stub.units
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(PropState.FAILING, ctx.state)
    assertEquals("abc", ctx.propValidated.name)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNameTrim(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = " \n \t abc \t\n   ",
            description = stub.description,
            unitMain = stub.unitMain,
            units = stub.units
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(PropState.FAILING, ctx.state)
    assertEquals("abc", ctx.propValidated.name)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNameEmpty(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = "",
            description = "abc",
            unitMain = stub.unitMain,
            units = stub.units
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(PropState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("name", error?.field)
    assertContains(error?.message ?: "", "name")
    assertEquals("validation-name-empty", error?.code)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNameSymbols(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = "!@#\$%^&*(),.{}",
            description = "abc",
            unitMain = stub.unitMain,
            units = stub.units
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(PropState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("name", error?.field)
    assertContains(error?.message ?: "", "name")
    assertEquals("validation-name-noContent", error?.code)
}