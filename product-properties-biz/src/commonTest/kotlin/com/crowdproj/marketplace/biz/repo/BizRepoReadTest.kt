package com.crowdproj.marketplace.biz.repo

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.repo.ProductPropertiesResponse
import com.crowdproj.marketplace.repository.tests.PropRepositoryMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {
    private val command = PropCommand.READ
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
            }
        )
    }
    private val settings by lazy {
        PropCorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { ProductPropertyProcessor(settings) }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = PropContext(
            command = command,
            state = PropState.NONE,
            workMode = PropWorkMode.TEST,
            propertiesRequest = mutableListOf(
                ProductProperty(
                    id = ProductPropertyId("123"),
                )
            ),
        )
        processor.exec(ctx)
        assertEquals(PropState.FINISHING, ctx.state)
        assertEquals(initProp.id, ctx.propertiesResponse.first().id)
        assertEquals(initProp.name, ctx.propertiesResponse.first().name)
        assertEquals(initProp.description, ctx.propertiesResponse.first().description)
        assertEquals(initProp.units, ctx.propertiesResponse.first().units)
        assertEquals(initProp.unitMain, ctx.propertiesResponse.first().unitMain)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest()
}