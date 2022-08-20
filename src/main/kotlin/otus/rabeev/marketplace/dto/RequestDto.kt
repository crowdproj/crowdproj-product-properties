package otus.rabeev.marketplace.dto

import otus.rabeev.marketplace.annotations.UUID

data class RequestDto(
    @get: UUID
    val id: String
)