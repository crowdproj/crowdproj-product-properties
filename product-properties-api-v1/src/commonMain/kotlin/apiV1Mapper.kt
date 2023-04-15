import com.crowdproj.marketplace.api.v1.models.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val apiV1Mapper = Json {
    classDiscriminator = "_"
    encodeDefaults = true
    ignoreUnknownKeys = true

    serializersModule = SerializersModule {

        polymorphic(IProductPropertyRequest::class) {
            subclass(ProductPropertyCreateRequest::class)
            subclass(ProductPropertyReadRequest::class)
            subclass(ProductPropertyUpdateRequest::class)
            subclass(ProductPropertyDeleteRequest::class)
            subclass(ProductPropertySearchRequest::class)
        }

        polymorphic(IProductPropertyResponse::class) {
            subclass(ProductPropertyCreateResponse::class)
            subclass(ProductPropertyReadResponse::class)
            subclass(ProductPropertyUpdateResponse::class)
            subclass(ProductPropertyDeleteResponse::class)
            subclass(ProductPropertySearchResponse::class)
        }

    }
}