package com.crowdproj.marketplace.biz.auth

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.permissions.ProductPropertyPermissionClient
import com.crowdproj.marketplace.common.permissions.PropPrincipalModel
import com.crowdproj.marketplace.common.permissions.PropUserGroups
import com.crowdproj.marketplace.repository.inmemory.PropRepoInMemory
import com.crowdproj.marketplace.stubs.PropStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class PropCrudAuthTest {
    @Test
    fun createSuccessTest() = runTest {
        val userId = PropUserId("123")
        val repo = PropRepoInMemory()
        val processor = ProductPropertyProcessor(
            settings = PropCorSettings(
                repoTest = repo
            )
        )
        val context = PropContext(
            workMode = PropWorkMode.TEST,
            propertyRequest = PropStub.prepareResult {
                permissionsClient.clear()
                id = ProductPropertyId.NONE
                ownerId = userId
            },
            command = PropCommand.CREATE,
            principal = PropPrincipalModel(
                id = userId,
                groups = setOf(
                    PropUserGroups.USER,
                    PropUserGroups.TEST,
                )
            )
        )

        processor.exec(context)
        assertEquals(PropState.FINISHING, context.state)
        with(context.propertyResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, ProductPropertyPermissionClient.READ)
            assertContains(permissionsClient, ProductPropertyPermissionClient.UPDATE)
            assertContains(permissionsClient, ProductPropertyPermissionClient.DELETE)
            assertContains(permissionsClient, ProductPropertyPermissionClient.SEARCH)
        }
    }

    @Test
    fun readSuccessTest() = runTest {
        val propertyObj = PropStub.get()
        val userId = PropUserId("123")
        val propertyId = propertyObj.id
        val repo = PropRepoInMemory(initObjects = listOf(propertyObj))
        val processor = ProductPropertyProcessor(
            settings = PropCorSettings(
                repoTest = repo
            )
        )
        val context = PropContext(
            command = PropCommand.READ,
            workMode = PropWorkMode.TEST,
            propertiesRequest = mutableListOf(ProductProperty(id = propertyId)),
            principal = PropPrincipalModel(
                id = userId,
                groups = setOf(
                    PropUserGroups.USER,
                    PropUserGroups.TEST,
                )
            )
        )
        processor.exec(context)
        assertEquals(PropState.FINISHING, context.state)
        with(context.propertiesResponse.first()) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, ProductPropertyPermissionClient.READ)
            assertContains(permissionsClient, ProductPropertyPermissionClient.SEARCH)
            assertFalse { permissionsClient.contains(ProductPropertyPermissionClient.UPDATE) }
            assertFalse { permissionsClient.contains(ProductPropertyPermissionClient.DELETE) }
        }
    }

    @Test
    fun updateSuccessTest() = runTest {
        val userId = PropUserId("123")
        val repo = PropRepoInMemory(initObjects = listOf(PropStub.get()))
        val processor = ProductPropertyProcessor(
            settings = PropCorSettings(
                repoTest = repo
            )
        )
        val context = PropContext(
            workMode = PropWorkMode.TEST,
            propertyRequest = PropStub.prepareResult {
                permissionsClient.clear()
                name = "update-name"
                description = "updated-description"
            },
            command = PropCommand.UPDATE,
            principal = PropPrincipalModel(
                id = userId,
                groups = setOf(
                    PropUserGroups.ADMIN_MP,
                    PropUserGroups.TEST,
                )
            )
        )

        processor.exec(context)
        assertEquals(PropState.FINISHING, context.state)
        with(context.propertyResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, ProductPropertyPermissionClient.READ)
            assertContains(permissionsClient, ProductPropertyPermissionClient.UPDATE)
            assertContains(permissionsClient, ProductPropertyPermissionClient.DELETE)
            assertContains(permissionsClient, ProductPropertyPermissionClient.SEARCH)
        }
    }

    @Test
    fun updateFailTest() = runTest {
        val userId = PropUserId("123")
        val repo = PropRepoInMemory(initObjects = listOf(PropStub.get()))
        val processor = ProductPropertyProcessor(
            settings = PropCorSettings(
                repoTest = repo
            )
        )
        val context = PropContext(
            workMode = PropWorkMode.TEST,
            propertyRequest = PropStub.prepareResult {
                permissionsClient.clear()
                name = "update-name"
                description = "updated-description"
            },
            command = PropCommand.UPDATE,
            principal = PropPrincipalModel(
                id = userId,
                groups = setOf(
                    PropUserGroups.USER,
                    PropUserGroups.TEST,
                )
            )
        )

        processor.exec(context)
        assertEquals(PropState.FAILING, context.state)
        assertEquals("User is not allowed to perform this operation", context.errors.first().message)
    }

    @Test
    fun deleteSuccessTest() = runTest {
        val userId = PropUserId("123")
        val productProperty = PropStub.prepareResult {
            ownerId = userId
        }
        val repo = PropRepoInMemory(initObjects = listOf(productProperty))
        val processor = ProductPropertyProcessor(
            settings = PropCorSettings(
                repoTest = repo
            )
        )
        val context = PropContext(
            workMode = PropWorkMode.TEST,
            propertyRequest = productProperty.apply {
                permissionsClient.clear()
            },
            command = PropCommand.DELETE,
            principal = PropPrincipalModel(
                id = userId,
                groups = setOf(
                    PropUserGroups.USER,
                    PropUserGroups.TEST,
                )
            )
        )

        processor.exec(context)
        assertEquals(PropState.FINISHING, context.state)
        with(context.propertyResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, ProductPropertyPermissionClient.READ)
            assertContains(permissionsClient, ProductPropertyPermissionClient.UPDATE)
            assertContains(permissionsClient, ProductPropertyPermissionClient.DELETE)
            assertContains(permissionsClient, ProductPropertyPermissionClient.SEARCH)
        }
    }

    @Test
    fun deleteFailTest() = runTest {
        val userId = PropUserId("123")
        val repo = PropRepoInMemory(initObjects = listOf(PropStub.get()))
        val processor = ProductPropertyProcessor(
            settings = PropCorSettings(
                repoTest = repo
            )
        )
        val context = PropContext(
            workMode = PropWorkMode.TEST,
            propertyRequest = PropStub.prepareResult {
                permissionsClient.clear()
            },
            command = PropCommand.DELETE,
            principal = PropPrincipalModel(
                id = userId,
                groups = setOf(
                    PropUserGroups.USER,
                    PropUserGroups.TEST,
                )
            )
        )

        processor.exec(context)
        assertEquals(PropState.FAILING, context.state)
        assertEquals("User is not allowed to perform this operation", context.errors.first().message)
    }

    @Test
    fun searchSuccessTest() = runTest {
        val userId = PropUserId("123")

        val repo = PropRepoInMemory(initObjects = PropStub.getList())
        val processor = ProductPropertyProcessor(
            settings = PropCorSettings(
                repoTest = repo
            )
        )
        val context = PropContext(
            workMode = PropWorkMode.TEST,
            propertiesFilterRequest = ProductPropertyFilter(name = "Length"),
            command = PropCommand.SEARCH,
            principal = PropPrincipalModel(
                id = userId,
                groups = setOf(
                    PropUserGroups.USER,
                    PropUserGroups.TEST,
                )
            )
        )

        processor.exec(context)
        assertEquals(PropState.FINISHING, context.state)
        with(context.propertiesResponse.first()) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, ProductPropertyPermissionClient.READ)
            assertContains(permissionsClient, ProductPropertyPermissionClient.SEARCH)
            assertFalse { permissionsClient.contains(ProductPropertyPermissionClient.UPDATE) }
            assertFalse { permissionsClient.contains(ProductPropertyPermissionClient.DELETE) }
        }
    }
}