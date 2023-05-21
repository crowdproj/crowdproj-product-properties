package com.crowdproj.marketplace.biz.repo

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.repo.ProductPropertiesResponse
import com.crowdproj.marketplace.repository.tests.PropRepositoryMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals

private val initProp = ProductProperty(
    id = ProductPropertyId("123"),
    name = "init",
    description = "init description",
    unitMain = ProductUnitId("100"),
    units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))
)
private val repo = PropRepositoryMock(
    invokeReadProp = {
        if (it.ids.contains(initProp.id)) {
            ProductPropertiesResponse(
                isSuccess = true,
                data = listOf(initProp),
            )
        } else ProductPropertiesResponse(
            isSuccess = false,
            data = null,
            errors = listOf(PropError(message = "Not found", field = "id"))
        )
    }
)
private val settings by lazy {
    PropCorSettings(
        repoTest = repo
    )
}
private val processor by lazy { ProductPropertyProcessor(settings) }

@OptIn(ExperimentalCoroutinesApi::class)
fun repoNotFoundTest(command: PropCommand) = runTest {
    val ctx = PropContext(
        command = command,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertyRequest = ProductProperty(
            id = ProductPropertyId("12345"),
            name = "xyz",
            description = "xyz",
            unitMain = ProductUnitId("100"),
            units = listOf(
                ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300")
            ),
            lock = ProductPropertyLock("123-234-abc-ABC"),
        )
    )
    processor.exec(ctx)
    assertEquals(PropState.FAILING, ctx.state)
    assertEquals(ProductProperty(), ctx.propertyResponse)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun repoNotFoundTest() = runTest {
    val ctx = PropContext(
        command = PropCommand.READ,
        state = PropState.NONE,
        workMode = PropWorkMode.TEST,
        propertiesRequest = mutableListOf(
            ProductProperty(
                id = ProductPropertyId("12345"),
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(PropState.FAILING, ctx.state)
    assertEquals(ProductProperty(), ctx.propertyResponse)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}