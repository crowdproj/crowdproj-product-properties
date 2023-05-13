import com.crowdproj.marketplace.api.v1.models.*
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.stubs.PropStubs
import com.crowdproj.marketplace.mappers.v1.fromTransport
import com.crowdproj.marketplace.mappers.v1.toTransportProductProperty
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperUpdateTest {
    @Test
    fun fromTransport() {
        val req = ProductPropertyUpdateRequest(
            requestId = "1234",
            debug = CpBaseDebug(
                mode = CpRequestDebugMode.STUB,
                stub = CpRequestDebugStubs.SUCCESS,
            ),
            productProperty = ProductPropertyUpdateObject(
                id = "111",
                lock = "222",
                name = "name",
                description = "desc",
                unitMain = "100",
                units = listOf("100", "200", "300"),
            ),
        ) as IProductPropertyRequest

        val context = PropContext()
        context.fromTransport(req)

        assertEquals(PropStubs.SUCCESS, context.stubCase)
        assertEquals(PropWorkMode.STUB, context.workMode)
        assertEquals(PropCommand.UPDATE, context.command)
        assertEquals("name", context.propertyRequest.name)
        assertEquals("desc", context.propertyRequest.description)
        assertEquals(ProductUnitId("100"), context.propertyRequest.unitMain)
        assertEquals(
            listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300")),
            context.propertyRequest.units
        )
        assertEquals("111", context.propertyRequest.id.asString())
        assertEquals("222", context.propertyRequest.lock.asString())
    }

    @Test
    fun toTransport() {
        val context = PropContext(
            requestId = PropRequestId("1234"),
            command = PropCommand.UPDATE,
            propertyResponse = ProductProperty(
                id = ProductPropertyId("111"),
                name = "name",
                description = "desc",
                unitMain = ProductUnitId("100"),
                units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300")),
                lock = ProductPropertyLock("222")
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

        val req = context.toTransportProductProperty() as ProductPropertyUpdateResponse

        assertEquals("1234", req.requestId)
        assertEquals("name", req.productProperty?.name)
        assertEquals("desc", req.productProperty?.description)
        assertEquals("100", req.productProperty?.unitMain)
        assertEquals("111", req.productProperty?.id)
        assertEquals("222", req.productProperty?.lock)
        assertEquals(listOf("100", "200", "300"), req.productProperty?.units)


        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.title)
        assertEquals("test exception", req.errors?.firstOrNull()?.description)
    }
}