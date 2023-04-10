import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductUnitId

object PropStub {
    val length = ProductProperty(
        id = ProductPropertyId("1"),
        name = "Length",
        description = "Length description",
        unitMain = ProductUnitId("100"),
        units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))
    )

    val weight = ProductProperty(
        id = ProductPropertyId("2"),
        name = "Weight",
        description = "Weight description",
        unitMain = ProductUnitId("100"),
        units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))
    )

    val height = ProductProperty(
        id = ProductPropertyId("3"),
        name = "Height",
        description = "Height description",
        unitMain = ProductUnitId("100"),
        units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))
    )

    val properties = listOf(length, weight, height)
}