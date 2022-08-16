package otus.rabeev.marketplace.dto

import otus.rabeev.marketplace.annotations.UUID
import javax.validation.constraints.NotNull

data class RequestDto(
    @NotNull
    @UUID
    val id: String
)