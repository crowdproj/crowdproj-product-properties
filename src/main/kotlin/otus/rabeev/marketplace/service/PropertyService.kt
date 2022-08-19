package otus.rabeev.marketplace.service

import org.springframework.stereotype.Service
import otus.rabeev.marketplace.dto.RequestDto
import otus.rabeev.marketplace.dto.ResponseDto

@Service
class PropertyService {
    fun propertyFromDB(requestDto: RequestDto): ResponseDto {
//        TODO("service that take properties from DB")


        return ResponseDto(
            id = "id",
            name = "name",
            description = "description",
            units = "10",
            unitMain = "unitMain"
        )
    }
}