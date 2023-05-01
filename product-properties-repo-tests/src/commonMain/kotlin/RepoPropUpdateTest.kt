package com.crowdproj.marketplace.repository.tests

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductUnitId
import com.crowdproj.marketplace.common.repo.IPropRepository
import com.crowdproj.marketplace.common.repo.ProductPropertyRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoPropUpdateTest {
    abstract val repo: IPropRepository
    protected open val updateSuccess = initObjects[0]
    private val updateIdNotFound = ProductPropertyId("ad-repo-update-not-found")

    private val updateObj by lazy {
        ProductProperty(
            id = updateSuccess.id,
            name = "update object",
            description = "update object description",
            unitMain = ProductUnitId("100"),
            units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))
        )
    }

    private val updateNotFound = ProductProperty(
        id = updateIdNotFound,
        name = "update object not found",
        description = "update object description not found",
        unitMain = ProductUnitId("400"),
        units = listOf(ProductUnitId("400"))
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateProductProperty(ProductPropertyRequest(updateObj))
        assertEquals(true, result.isSuccess)
        assertEquals(updateObj.id, result.data?.id)
        assertEquals(updateObj.name, result.data?.name)
        assertEquals(updateObj.description, result.data?.description)
        assertEquals(updateObj.unitMain, result.data?.unitMain)
        assertEquals(updateObj.units, result.data?.units)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateProductProperty(ProductPropertyRequest(updateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitProductProperties("update") {
        override val initObjects: List<ProductProperty> = listOf(
            createInitTestModel("update"),
            createInitTestModel("update2"),
        )
    }
}