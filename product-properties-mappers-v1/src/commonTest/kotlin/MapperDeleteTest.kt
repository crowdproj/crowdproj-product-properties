import com.crowdproj.marketplace.api.v1.models.*
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.stubs.PropStubs
import com.crowdproj.marketplace.mappers.v1.fromTransport
import com.crowdproj.marketplace.mappers.v1.toTransportProductProperty
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperDeleteTest {
    @Test
    fun fromTransport() {
        val req = ProductPropertyDeleteRequest(
            requestType = "update",
            requestId = "1234",
            debug = CpBaseDebug(
                mode = CpRequestDebugMode.STUB,
                stub = CpRequestDebugStubs.SUCCESS,
            ),
            productPropertyId = "111",
            lock = "222",
        ) as IProductPropertyRequest

        val context = PropContext()
        context.fromTransport(req)

        assertEquals(PropStubs.SUCCESS, context.stubCase)
        assertEquals(PropWorkMode.STUB, context.workMode)
        assertEquals(PropCommand.DELETE, context.command)
        assertEquals("111", context.propertyRequest.id.asString())
        assertEquals("222", context.propertyRequest.lock.asString())
    }

    @Test
    fun toTransport() {
        val context = PropContext(
            requestId = PropRequestId("1234"),
            command = PropCommand.DELETE,
            propertyResponse = ProductProperty(
                id = ProductPropertyId("111"),
                name = "name",
                description = "desc",
                unitMain = ProductUnitId("100"),
                units = listOf(ProductUnitId("100"), ProductUnitId("200"), ProductUnitId("300")),
                deleted = true,
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

        val req = context.toTransportProductProperty() as ProductPropertyDeleteResponse

        assertEquals("1234", req.requestId)
        assertEquals("name", req.productProperty?.name)
        assertEquals("desc", req.productProperty?.description)
        assertEquals("100", req.productProperty?.unitMain)
        assertEquals("111", req.productProperty?.id)
        assertEquals("222", req.productProperty?.lock)
        assertEquals(true, req.productProperty?.deleted)
        assertEquals(listOf("100", "200", "300"), req.productProperty?.units)


        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.title)
        assertEquals("test exception", req.errors?.firstOrNull()?.description)
    }
}