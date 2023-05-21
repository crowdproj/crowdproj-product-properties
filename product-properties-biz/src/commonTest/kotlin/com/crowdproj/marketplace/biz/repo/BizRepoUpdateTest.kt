package com.crowdproj.marketplace.biz.repo

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.repo.ProductPropertiesResponse
import com.crowdproj.marketplace.common.repo.ProductPropertyResponse
import com.crowdproj.marketplace.repository.tests.PropRepositoryMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoUpdateTest {
    private val command = PropCommand.UPDATE
    private val initProp = ProductProperty(
        id = ProductPropertyId("123"),
        name = "init",
        description = "init description",
        unitMain = ProductUnitId("100"),
        units = listOf(
            ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300")
        )
    )
    private val repo by lazy {
        PropRepositoryMock(
            invokeReadProp = {
                ProductPropertiesResponse(
                    isSuccess = true,
                    data = listOf(initProp),
                )
            },
            invokeUpdateProp = {
                ProductPropertyResponse(
                    isSuccess = true,
                    data = ProductProperty(
                        id = ProductPropertyId("123"),
                        name = "xyz",
                        description = "xyz",
                        unitMain = ProductUnitId("100"),
                        units = listOf(
                            ProductUnitId("100")
                        )
                    )
                )
            }
        )
    }
    private val settings by lazy {
        PropCorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { ProductPropertyProcessor(settings) }

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val propToUpdate = ProductProperty(
            id = ProductPropertyId("123"),
            name = "xyz",
            description = "xyz",
            unitMain = ProductUnitId("100"),
            units = listOf(
                ProductUnitId("100")
            ),
            lock = ProductPropertyLock("123-234-abc-ABC"),
        )
        val ctx = PropContext(
            command = command,
            state = PropState.NONE,
            workMode = PropWorkMode.TEST,
            propertyRequest = propToUpdate,
        )
        processor.exec(ctx)
        assertEquals(PropState.FINISHING, ctx.state)
        assertEquals(propToUpdate.id, ctx.propertyResponse.id)
        assertEquals(propToUpdate.name, ctx.propertyResponse.name)
        assertEquals(propToUpdate.description, ctx.propertyResponse.description)
        assertEquals(propToUpdate.unitMain, ctx.propertyResponse.unitMain)
        assertEquals(propToUpdate.units, ctx.propertyResponse.units)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}