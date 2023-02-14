rootProject.name = "crowdproj-product-properties"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
        id("io.kotest")
    }

}

include("m2l3-testing")