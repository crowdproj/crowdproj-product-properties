package otus.rabeev.marketplace.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import otus.rabeev.marketplace.dto.RequestDto
import otus.rabeev.marketplace.dto.ResponseDto
import otus.rabeev.marketplace.service.PropertyService
import javax.validation.Valid


@RestController
class Controller(private val propertyService: PropertyService) {

    @PostMapping("/property-request")
    fun propertyRequest(
        @RequestHeader headers: Map<String, String>,
        @Valid @RequestBody requestDto: RequestDto
    ): ResponseDto = propertyService.propertyFromDB(requestDto)
}