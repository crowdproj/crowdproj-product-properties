package otus.rabeev.marketplace.controllerTests

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.Captor
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import otus.rabeev.marketplace.dto.RequestDto
import otus.rabeev.marketplace.dto.ResponseDto
import otus.rabeev.marketplace.header.Header
import otus.rabeev.marketplace.service.PropertyService
import kotlin.reflect.full.declaredMemberProperties

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
//    properties = [
//        "spring.datasource.url=jdbc:h2:mem:testdb"
//    ]
)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ControllerTests() {

//    val header1 = """{"id":"{da72ec3b-a973-4563-9296-b5b28dcb99be}","name":"nameHeader","user":"userHeader","client":"clientHeader"}"""

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @MockBean
    lateinit var propertyService: PropertyService


    protected fun getHttpEntity(obj: Any, header: Any) = HttpEntity(obj, getHttpHeaders(header))

    protected fun getHttpEntity(obj: Any, headers: HttpHeaders) = HttpEntity(obj, headers)

    protected fun getHttpHeaders(header: Any) = HttpHeaders().apply {
        header::class.declaredMemberProperties.forEach { prop ->
            prop.getter.call(header)?.also { value -> set(prop.name, value.toString()) }
        }
    }

    fun checkHeader(reqHeader: Map<String, String?>, httpHeaders: HttpHeaders) {
        val resHeader = reqHeader.keys.map {
            it to (httpHeaders[it]?.firstOrNull() ?: "").toString()
        }.toMap()
        reqHeader.forEach {
            assertEquals(it.value?.trim() ?: "", resHeader[it.key]?.trim() ?: "", "error in header ${it.key}")
        }
        assertEquals(reqHeader.keys.size, resHeader.keys.size)
    }


    @Test
    fun `test controller propertyRequest`() {
        val requestDto = RequestDto("productId")
        val responseDto = ResponseDto(
            id = "id",
            name = "name",
            description = "description",
            units = "units",
            unitMain = "unitMain"
        )
        val header = Header.build(user = "user")

        given(propertyService.propertyFromDB(requestDto)).willReturn(responseDto)


        val response = testRestTemplate.exchange<ResponseDto>(
            "/property-request",
            HttpMethod.POST,
            getHttpEntity(requestDto, header)
        )


//        val headerRes = response.headers
        val dto = response.body
        assertNotNull(dto)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(responseDto, dto)
//        checkHeader()

    }
}
