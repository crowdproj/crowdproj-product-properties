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
abstract class RepoPropCreateTest {
    abstract val repo: IPropRepository

    private val createObj = ProductProperty(
        name = "create object",
        description = "create object description",
        unitMain = ProductUnitId("100"),
        units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createProductProperty(ProductPropertyRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: ProductPropertyId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.name, result.data?.name)
        assertEquals(expected.description, result.data?.description)
        assertEquals(expected.unitMain, result.data?.unitMain)
        assertEquals(expected.units, result.data?.units)

        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitProductProperties("create") {
        override val initObjects: List<ProductProperty> = emptyList()
    }
}