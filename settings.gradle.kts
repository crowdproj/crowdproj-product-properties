rootProject.name = "com.crowdproj.marketplace"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }

}

include("product-properties-api-v1")
include("product-properties-common")
include("product-properties-mappers-v1")
include("product-properties-app-ktor")
include("product-properties-stubs")