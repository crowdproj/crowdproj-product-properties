package com.crowdproj.marketplace.repository.tests

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.repo.IPropRepository
import com.crowdproj.marketplace.common.repo.ProductPropertyIdRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoPropDeleteTest {
    abstract val repo: IPropRepository
    protected open val deleteSuccess = initObjects[0]

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteProductProperty(ProductPropertyIdRequest(deleteSuccess.id))

        assertEquals(true, result.isSuccess)
        assertEquals(true, result.data?.deleted)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.deleteProductProperty(ProductPropertyIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitProductProperties("delete") {
        override val initObjects: List<ProductProperty> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
        val notFoundId = ProductPropertyId("prop-repo-delete-notFound")
    }
}