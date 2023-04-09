import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val serializationVersion: String by project
val logbackVersion: String by project
val jvmTarget: String by project

fun ktorIo(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    id("application")
    id("io.ktor.plugin")
    kotlin("plugin.serialization")
    kotlin("multiplatform")
}
val webjars: Configuration by configurations.creating
dependencies {
    val swaggerUiVersion: String by project
    webjars("org.webjars:swagger-ui:$swaggerUiVersion")
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

ktor {
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(io.ktor.plugin.features.JreVersion.valueOf("JRE_$jvmTarget"))
    }
}

kotlin {
    jvm {}
    linuxX64 {
        binaries {
            executable {
                entryPoint = "com.crowdproj.marketplace.app.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":product-properties-api-v1"))
                implementation(project(":product-properties-common"))
                implementation(project(":product-properties-mappers-v1"))
                implementation(project(":product-properties-stubs"))
                implementation(project(":product-properties-lib-logging-kermit"))
                implementation(project(":product-properties-api-log"))
                implementation(project(":product-properties-mappers-log"))

                implementation(ktorIo("core")) // "io.ktor:ktor-server-core:$ktorVersion"
                implementation(ktorIo("cio"))
                implementation(ktorIo("config-yaml"))
                implementation(ktorIo("websockets"))

                implementation(ktorIo("content-negotiation"))
                implementation(ktorIo("cors"))
                implementation(ktorIo("caching-headers"))
                implementation(ktorIo("auto-head-response"))
                implementation(ktorIo("kotlinx-json", prefix = "serialization-"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(ktorIo("test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
                implementation(ktorIo("content-negotiation", prefix = "client-"))
                implementation(ktorIo("websockets", prefix = "client-"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(ktorIo("call-logging"))
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation(project(":product-properties-lib-logging-logback"))
            }
        }
    }
    tasks {
        @Suppress("UnstableApiUsage")
        withType<ProcessResources>().configureEach {
            println("RESOURCES: ${this.name} ${this::class}")
            from("$rootDir/product-properties-api-v1/spec-product-properties-v1.yaml") {
                into("specs")
                filter {
                    // Устанавливаем версию в сваггере
                    it.replace("\${VERSION_APP}", project.version.toString())
                }
            }
            from("$rootDir/product-properties-api-v1/build/base.yaml") {
                into("specs")
            }
            webjars.forEach { jar ->
                val conf = webjars.resolvedConfiguration
                println("JarAbsPa: ${jar.absolutePath}")
                val artifact = conf.resolvedArtifacts.find { it.file.toString() == jar.absolutePath } ?: return@forEach
                val upStreamVersion = artifact.moduleVersion.id.version.replace("(-[\\d.-]+)", "")
                copy {
                    from(zipTree(jar))
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    into(file("${buildDir}/webjars-content/${artifact.name}"))
                }
                with(this@configureEach) {
                    this.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    from(
                        "${buildDir}/webjars-content/${artifact.name}/META-INF/resources/webjars/${artifact.name}/${upStreamVersion}"
                    ) { into(artifact.name) }
                    from(
                        "${buildDir}/webjars-content/${artifact.name}/META-INF/resources/webjars/${artifact.name}/${artifact.moduleVersion.id.version}"
                    ) { into(artifact.name) }
                }
            }
        }
    }
}