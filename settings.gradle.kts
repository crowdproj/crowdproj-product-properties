rootProject.name = "com.crowdproj.marketplace"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings
        val ktorPluginVersion: String by settings
        val codeGeneratorVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("io.ktor.plugin") version ktorPluginVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("com.crowdproj.generator") version codeGeneratorVersion apply false
    }

}

include("product-properties-api-v1")
include("product-properties-common")
include("product-properties-mappers-v1")
include("product-properties-app-ktor")
include("product-properties-stubs")
include("product-properties-api-log")
include("product-properties-lib-logging-common")
include("product-properties-lib-logging-kermit")
include("product-properties-lib-logging-logback")
include("product-properties-mappers-log")
include("product-properties-biz")
include("product-properties-repo-stubs")
include("product-properties-repo-tests")
include("product-properties-repo-in-memory")
include("product-properties-repo-gremlin")
include("product-properties-auth")