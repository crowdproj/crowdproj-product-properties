package otus.rabeev.marketplace.header

import otus.rabeev.marketplace.annotations.UUID
import javax.validation.constraints.NotBlank

data class Header(
    @get:UUID
    val id: String,
    @get:NotBlank
    val user: String,
    @get:NotBlank
    val client: String
) {
    companion object {
        fun build(
            user: String,
            client: String = "crm",
            id: String = "{${java.util.UUID.randomUUID()}}",
        ) = Header(id = id, user = user, client = client)
    }
}