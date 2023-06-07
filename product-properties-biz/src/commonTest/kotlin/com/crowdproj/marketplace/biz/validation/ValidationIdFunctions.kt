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
fun validationIdCorrect(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = ProductPropertyId("123-234-abc-ABC"),
            name = "abc",
            description = stub.description,
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
fun validationIdTrim(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = ProductPropertyId(" \n\t 123-234-abc-ABC \n\t "),
            name = "abc",
            description = stub.description,
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
    assertEquals(ProductPropertyId("123-234-abc-ABC"), ctx.propValidated.id)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdEmpty(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = ProductPropertyId(""),
            name = "abc",
            description = stub.description,
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
    assertEquals(1, ctx.errors.size)
    assertEquals(PropState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
    assertEquals("validation-id-empty", error?.code)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdFormat(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = ProductPropertyId("!@#\$%^&*(),.{}"),
            name = "abc",
            description = stub.description,
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
    assertEquals(1, ctx.errors.size)
    assertEquals(PropState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
    assertEquals("validation-id-badFormat", error?.code)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdsCorrect(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertiesRequest = mutableListOf(
            ProductProperty(
                id = ProductPropertyId("123-234-abc-ABC"),
                name = "abc",
                description = stub.description,
                unitMain = stub.unitMain,
                units = stub.units,
                lock = ProductPropertyLock("123-234-abc-ABC"),
            )
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
fun validationIdsTrim(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertiesRequest = mutableListOf(
            ProductProperty(
                id = ProductPropertyId(" \n\t 123-234-abc-ABC \n\t "),
                name = "abc",
                description = stub.description,
                unitMain = stub.unitMain,
                units = stub.units,
                lock = ProductPropertyLock("123-234-abc-ABC"),
            )
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
    assertEquals(ProductPropertyId("123-234-abc-ABC"), ctx.propsValidated.first().id)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdsEmpty(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertiesRequest = mutableListOf(
            ProductProperty(
                id = ProductPropertyId(""),
                name = "abc",
                description = stub.description,
                unitMain = stub.unitMain,
                units = stub.units,
                lock = ProductPropertyLock("123-234-abc-ABC"),
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(PropState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("ids", error?.field)
    assertContains(error?.message ?: "", "ids")
    assertEquals("validation-ids-empty", error?.code)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdsFormat(command: PropCommand, processor: ProductPropertyProcessor) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertiesRequest = mutableListOf(
            ProductProperty(
                id = ProductPropertyId("!@#\$%^&*(),.{}"),
                name = "abc",
                description = stub.description,
                unitMain = stub.unitMain,
                units = stub.units,
                lock = ProductPropertyLock("123-234-abc-ABC"),
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(PropState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("ids", error?.field)
    assertContains(error?.message ?: "", "ids")
    assertEquals("validation-ids-badFormat", error?.code)
}