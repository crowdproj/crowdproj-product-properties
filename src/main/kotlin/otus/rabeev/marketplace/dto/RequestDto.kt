package otus.rabeev.marketplace.dto

import otus.rabeev.marketplace.annotations.UUID
import javax.validation.constraints.NotBlank

data class RequestDto(
    @get: UUID
    val id: String
)