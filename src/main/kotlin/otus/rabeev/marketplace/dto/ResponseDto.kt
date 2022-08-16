package otus.rabeev.marketplace.dto

import otus.rabeev.marketplace.annotations.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class ResponseDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val units: String = "",
    val unitMain: String = ""
)
