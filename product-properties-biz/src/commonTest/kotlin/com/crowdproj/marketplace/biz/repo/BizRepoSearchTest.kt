package com.crowdproj.marketplace.biz.repo

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.permissions.PropPrincipalModel
import com.crowdproj.marketplace.common.permissions.PropUserGroups
import com.crowdproj.marketplace.common.repo.ProductPropertiesResponse
import com.crowdproj.marketplace.repository.tests.PropRepositoryMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {
    private val userId = PropUserId("321")
    private val command = PropCommand.SEARCH
    private val initProp = ProductProperty(
        id = ProductPropertyId("123"),
        name = "init",
        description = "init description",
        unitMain = ProductUnitId("100"),
        units = listOf(
            ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300")
        ),
        ownerId = userId
    )
    private val repo by lazy {
        PropRepositoryMock(
            invokeSearchProp = {
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
    fun repoSearchSuccessTest() = runTest {
        val ctx = PropContext(
            command = command,
            state = PropState.NONE,
            workMode = PropWorkMode.TEST,
            propertiesFilterRequest = ProductPropertyFilter(
                name = "ab",
                description = ""
            ),
            principal = PropPrincipalModel(
                id = userId,
                groups = setOf(
                    PropUserGroups.USER,
                    PropUserGroups.TEST,
                )
            ),
        )
        processor.exec(ctx)
        assertEquals(PropState.FINISHING, ctx.state)
        assertEquals(1, ctx.propertiesResponse.size)
    }
}