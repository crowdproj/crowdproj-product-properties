plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
    linuxX64 {}

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation ("com.benasher44:uuid:0.7.0")
                implementation(project(":product-properties-common"))
            }
        }
    }
}
