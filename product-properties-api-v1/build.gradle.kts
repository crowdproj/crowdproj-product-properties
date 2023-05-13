plugins {
    kotlin("multiplatform")
    id("com.crowdproj.generator")
}

val apiVersion = "v1"
val apiSpec: Configuration by configurations.creating
val crowdProjBaseApiVersion: String by project
dependencies {
    apiSpec(
        group = "com.crowdproj",
        name = "specs-v0",
        version = crowdProjBaseApiVersion,
        classifier = "openapi",
        ext = "yaml"
    )
}

kotlin {
    jvm { }
    linuxX64 { }

    sourceSets {
        val serializationVersion: String by project

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {

            kotlin.srcDirs("$buildDir/generate-resources/main/src/commonMain/kotlin")

            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

crowdprojGenerate {
    packageName.set("${project.group}.api.v1")
    inputSpec.set("${project.buildDir}/spec-product-properties-$apiVersion.yaml")
}

val getSpecs: Task by tasks.creating {
    doFirst {
        copy {
            from("${rootProject.projectDir}/specs")
            into(project.buildDir.toString())
        }
        copy {
            from(apiSpec.asPath)
            into("$buildDir")
            rename { "base.yaml" }
        }
    }
}

tasks {
    this.openApiGenerate {
        dependsOn(getSpecs)
    }
}

afterEvaluate {
    val openApiGenerate = tasks.getByName("openApiGenerate")
    tasks.filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(openApiGenerate)
    }
    tasks.filter { it.name.endsWith("Elements") }.forEach {
        it.dependsOn(openApiGenerate)
    }
}
