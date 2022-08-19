package otus.rabeev.marketplace.controllerTests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import otus.rabeev.marketplace.dto.RequestDto
import otus.rabeev.marketplace.dto.ResponseDto
import otus.rabeev.marketplace.service.PropertyService

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
class ControllerTests() {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @MockBean
    lateinit var propertyService: PropertyService


    @Test
    fun `test controller propertyRequest success`() {
        val requestDto = RequestDto("123e4567-e89b-12d3-a456-426655440001")
        val responseDto = ResponseDto(
            id = "id is correct",
            name = "name",
            description = "description",
            units = "units",
            unitMain = "unitMain"
        )

        given(propertyService.propertyFromDB(requestDto)).willReturn(responseDto)


        val response = testRestTemplate.exchange<ResponseDto>(
            "/property-request",
            HttpMethod.POST,
            getHttpEntity(requestDto)
        )


        val dto = response.body
        assertNotNull(dto)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(responseDto, dto)

    }

    @Test
    fun `test controller propertyRequest with incorrect RequestDto`() {
        val requestDto = RequestDto("wrong format")
        val responseDto = ResponseDto()


        val response = testRestTemplate.exchange<ResponseDto>(
            "/property-request",
            HttpMethod.POST,
            getHttpEntity(requestDto)
        )


        val dto = response.body
        assertNotNull(dto)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(responseDto, dto)

    }

    protected fun getHttpEntity(obj: Any) = HttpEntity(obj)
}
