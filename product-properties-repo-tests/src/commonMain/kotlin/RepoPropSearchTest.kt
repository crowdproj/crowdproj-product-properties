package com.crowdproj.marketplace.repository.tests

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.repo.IPropRepository
import com.crowdproj.marketplace.common.repo.ProductPropertyFilterRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoPropSearchTest {
    abstract val repo: IPropRepository

    protected open val initializedObjects: List<ProductProperty> = initObjects

    @Test
    fun searchByName() = runRepoTest {
        val result = repo.searchProductProperty(ProductPropertyFilterRequest(nameFilter = "stub"))
        assertEquals(true, result.isSuccess)
        assertEquals(initializedObjects.sortedBy { it.id.asString() }, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchByDescription() = runRepoTest {
        val result = repo.searchProductProperty(ProductPropertyFilterRequest(descriptionFilter = "stub"))
        assertEquals(true, result.isSuccess)
        assertEquals(initializedObjects.sortedBy { it.id.asString() }, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitProductProperties("search") {
        override val initObjects: List<ProductProperty> = listOf(
            createInitTestModel("prop1"),
            createInitTestModel("prop2"),
            createInitTestModel("prop3"),
            createInitTestModel("prop4"),
            createInitTestModel("prop5"),
        )
    }
}