import com.crowdproj.marketplace.api.logs.mapper.toLog
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class PropContextToLogMapperTest {
    @Test
    fun fromContext() {
        val context = PropContext(
            requestId = PropRequestId("1234"),
            command = PropCommand.CREATE,
            propertyResponse = ProductProperty(
                name = "name",
                description = "desc",
                unitMain = ProductUnitId("100"),
                units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300"))
            ),
            errors = mutableListOf(
                PropError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                    exception = Exception("test exception")
                )
            ),
            state = PropState.RUNNING,
        )

        val log = context.toLog("test-id")

        assertEquals("test-id", log.logId)
        assertEquals("crowdproj-product-property", log.source)
        assertEquals("1234", log.productProperty?.requestId)
        assertEquals("CREATE", log.productProperty?.operation?.name)

        val productProperty = log.productProperty?.responseProductProp
        assertEquals("name", productProperty?.name)
        assertEquals("desc", productProperty?.description)
        assertEquals("100", productProperty?.unitMain)
        assertEquals(listOf("100", "200", "300"), productProperty?.units)

        val error = log.errors?.firstOrNull()
        assertEquals("wrong title", error?.message)
        assertEquals("ERROR", error?.level)
        assertEquals("err", error?.code)
        assertEquals("title", error?.field)
    }
}