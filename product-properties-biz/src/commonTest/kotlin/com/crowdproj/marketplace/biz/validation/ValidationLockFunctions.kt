package com.crowdproj.marketplace.biz.validation

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.permissions.PropPrincipalModel
import com.crowdproj.marketplace.common.permissions.PropUserGroups
import com.crowdproj.marketplace.stubs.PropStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = PropStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockCorrect(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = "abc",
            description = "abc",
            unitMain = stub.unitMain,
            units = stub.units,
            lock = ProductPropertyLock("123-234-abc-ABC"),
        ),
        principal = PropPrincipalModel(
            id = stub.ownerId,
            groups = setOf(
                PropUserGroups.USER,
                PropUserGroups.TEST,
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(PropState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockTrim(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = "abc",
            description = "abc",
            unitMain = stub.unitMain,
            units = stub.units,
            lock = ProductPropertyLock(" \n\t 123-234-abc-ABC \n\t "),
        ),
        principal = PropPrincipalModel(
            id = stub.ownerId,
            groups = setOf(
                PropUserGroups.USER,
                PropUserGroups.TEST,
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(PropState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockEmpty(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = "abc",
            description = "abc",
            unitMain = stub.unitMain,
            units = stub.units,
            lock = ProductPropertyLock(""),
        ),
    )

    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(PropState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockFormat(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = stub.id,
            name = "abc",
            description = "abc",
            unitMain = stub.unitMain,
            units = stub.units,
            lock = ProductPropertyLock("!@#\$%^&*(),.{}"),
        ),
    )

    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(PropState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}