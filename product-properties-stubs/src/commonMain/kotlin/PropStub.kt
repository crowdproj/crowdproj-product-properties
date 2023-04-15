import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductUnitId

object PropStub {
    private val length = ProductProperty(
        id = ProductPropertyId("1"),
        name = "Length",
        description = "Length description",
        unitMain = ProductUnitId("100"),
        units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))
    )

    private val weight = ProductProperty(
        id = ProductPropertyId("2"),
        name = "Weight",
        description = "Weight description",
        unitMain = ProductUnitId("100"),
        units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))
    )

    private val height = ProductProperty(
        id = ProductPropertyId("3"),
        name = "Height",
        description = "Height description",
        unitMain = ProductUnitId("100"),
        units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))
    )

    fun get() = length

    fun getDeleted() = height.copy(deleted = true)

    fun getList() = listOf(length, weight, height).toMutableList()

    fun prepareResult(block: ProductProperty.() -> Unit): ProductProperty = get().apply(block)
}