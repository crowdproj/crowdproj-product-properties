import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val serializationVersion: String by project
val logbackVersion: String by project

fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    id("application")
    id("com.bmuschko.docker-java-application")
    id("com.bmuschko.docker-remote-api")
    kotlin("plugin.serialization")
    kotlin("multiplatform")
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
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

                implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"
                implementation(ktor("cio"))
                implementation(ktor("config-yaml"))
                implementation(ktor("websockets"))

                implementation(ktor("content-negotiation"))
                implementation(ktor("kotlinx-json", prefix = "serialization-"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(ktor("test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
                implementation(ktor("content-negotiation", prefix = "client-"))
                implementation(ktor("websockets", prefix = "client-"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(ktor("call-logging"))
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
            }
        }
    }
    tasks {
        val dockerJvmDockerfile by creating(Dockerfile::class) {
            group = "docker"
            from("openjdk:17")
            copyFile("app.jar", "app.jar")
            entryPoint("java", "-Xms256m", "-Xmx512m", "-jar", "/app.jar")
        }
        create("dockerBuildJvmImage", DockerBuildImage::class) {
            group = "docker"
            dependsOn(dockerJvmDockerfile, named("jvmJar"))
            doFirst {
                copy {
                    from(named("jvmJar"))
                    into("${project.buildDir}/docker/app.jar")
                }
            }
            images.add("${project.name}:${project.version}")
        }
    }
}