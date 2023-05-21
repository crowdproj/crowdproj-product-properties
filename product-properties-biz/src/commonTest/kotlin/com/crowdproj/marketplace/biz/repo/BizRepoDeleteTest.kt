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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoDeleteTest {
    private val command = PropCommand.DELETE
    private val initProp = ProductProperty(
        id = ProductPropertyId("123"),
        name = "init",
        description = "init description",
        unitMain = ProductUnitId("100"),
        units = listOf(
            ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300")
        ),
        lock = ProductPropertyLock("123-234-abc-ABC"),
    )
    private val repo by lazy {
        PropRepositoryMock(
            invokeReadProp = {
                ProductPropertiesResponse(
                    isSuccess = true,
                    data = listOf(initProp),
                )
            },
            invokeDeleteProp = {
                if (it.id == initProp.id)
                    ProductPropertyResponse(
                        isSuccess = true,
                        data = initProp.copy(deleted = true)
                    )
                else ProductPropertyResponse(isSuccess = false, data = null)
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
    fun repoDeleteSuccessTest() = runTest {
        val propToDelete = ProductProperty(
            id = ProductPropertyId("123"),
            lock = ProductPropertyLock("123-234-abc-ABC"),
        )
        val ctx = PropContext(
            command = command,
            state = PropState.NONE,
            workMode = PropWorkMode.TEST,
            propertyRequest = propToDelete,
        )
        processor.exec(ctx)
        assertEquals(PropState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initProp.id, ctx.propertyResponse.id)
        assertEquals(initProp.name, ctx.propertyResponse.name)
        assertEquals(initProp.description, ctx.propertyResponse.description)
        assertEquals(initProp.unitMain, ctx.propertyResponse.unitMain)
        assertEquals(initProp.units, ctx.propertyResponse.units)
        assertTrue(ctx.propertyResponse.deleted)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}