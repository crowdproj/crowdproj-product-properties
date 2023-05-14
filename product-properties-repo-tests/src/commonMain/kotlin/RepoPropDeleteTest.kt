package com.crowdproj.marketplace.repository.tests

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductPropertyLock
import com.crowdproj.marketplace.common.repo.IPropRepository
import com.crowdproj.marketplace.common.repo.ProductPropertyIdRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoPropDeleteTest {
    abstract val repo: IPropRepository
    protected open val deleteSuccess = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = ProductPropertyId("prop-repo-delete-notFound")
    protected val lockNew = ProductPropertyLock("20000000-0000-0000-0000-000000000002")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSuccess.lock
        val result = repo.deleteProductProperty(ProductPropertyIdRequest(deleteSuccess.id, lock = lockOld))

        assertEquals(true, result.isSuccess)
        assertEquals(true, result.data?.deleted)
        assertEquals(lockNew, result.data?.lock)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.deleteProductProperty(ProductPropertyIdRequest(notFoundId, lock = lockOld))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val lockOld = deleteSuccess.lock
        val result = repo.deleteProductProperty(ProductPropertyIdRequest(deleteConc.id, lock = lockBad))

        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(lockOld, result.data?.lock)
    }

    companion object : BaseInitProductProperties("delete") {
        override val initObjects: List<ProductProperty> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}