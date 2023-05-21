package com.crowdproj.marketplace.repository.tests

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.repo.IPropRepository
import com.crowdproj.marketplace.common.repo.ProductPropertiesIdsRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoPropReadTest {
    abstract val repo: IPropRepository
    protected open val readSuccess = initObjects

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readProductProperties(ProductPropertiesIdsRequest(readSuccess.map { it.id }))

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccess, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readProductProperties(ProductPropertiesIdsRequest(listOf(notFoundId)))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitProductProperties("read") {
        override val initObjects: List<ProductProperty> = listOf(
            createInitTestModel("prop1"),
            createInitTestModel("prop2"),
            createInitTestModel("prop3"),
        )

        val notFoundId = ProductPropertyId("prop-repo-read-notFound")

    }
}