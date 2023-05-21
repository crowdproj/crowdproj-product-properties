package com.crowdproj.marketplace.biz.repo

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.repo.ProductPropertyResponse
import com.crowdproj.marketplace.repository.tests.PropRepositoryMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {
    private val command = PropCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = PropRepositoryMock(
        invokeCreateProp = {
            ProductPropertyResponse(
                isSuccess = true,
                data = ProductProperty(
                    id = ProductPropertyId(uuid),
                    name = it.productProperty.name,
                    description = it.productProperty.description,
                    units = it.productProperty.units,
                    unitMain = it.productProperty.unitMain,
                )
            )
        }
    )
    private val settings = PropCorSettings(
        repoTest = repo
    )
    private val processor = ProductPropertyProcessor(settings)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoCreateSuccessTest() = runTest {
        val propertyToCreate = ProductProperty(
            name = "create test",
            description = "create test description",
            unitMain = ProductUnitId("100"),
            units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))
        )

        val ctx = PropContext(
            command = command,
            state = PropState.NONE,
            workMode = PropWorkMode.TEST,
            propertyRequest = propertyToCreate,
        )
        processor.exec(ctx)
        assertEquals(PropState.FINISHING, ctx.state)
        assertNotEquals(ProductPropertyId.NONE, ctx.propertyResponse.id)
        assertEquals(propertyToCreate.name, ctx.propertyResponse.name)
        assertEquals(propertyToCreate.description, ctx.propertyResponse.description)
        assertEquals(propertyToCreate.unitMain, ctx.propertyResponse.unitMain)
        assertEquals(propertyToCreate.units, ctx.propertyResponse.units)
    }
}