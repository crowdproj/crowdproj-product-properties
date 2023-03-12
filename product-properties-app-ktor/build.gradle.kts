import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val serializationVersion: String by project

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
    mainClass.set("com.crowdproj.marketplace.app.ApplicationKt")
}

kotlin {
    val nativeTarget = when (System.getProperty("os.name")) {
        "Mac OS X" -> macosX64("native")
        "Linux" -> linuxX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }
    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "com.crowdproj.marketplace.app.main"
            }
        }
    }
    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation(project(":product-properties-api-v1"))
                implementation(project(":product-properties-common"))
                implementation(project(":product-properties-mappers-v1"))
                implementation(project(":product-properties-stubs"))

                implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"
                implementation(ktor("cio"))

                implementation(ktor("content-negotiation"))
                implementation(ktor("kotlinx-json", prefix = "serialization-"))
            }
        }
        val nativeTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(ktor("test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
                implementation(ktor("content-negotiation", prefix = "client-"))
            }
        }
    }
    tasks {
        val linkReleaseExecutableNative by getting(KotlinNativeLink::class)

        val dockerDockerfile by creating(Dockerfile::class) {
            group = "docker"
            from("ubuntu:22.02")
            doFirst {
                copy {
                    from(linkReleaseExecutableNative.binary.outputFile)
                    into("${this@creating.temporaryDir}/app")
                }
            }
            copyFile("app", "/app")
            entryPoint("/app")
        }
        create("dockerBuildNativeImage", DockerBuildImage::class) {
            group = "docker"
            dependsOn(dockerDockerfile)
            images.add("${project.name}:${project.version}")
        }
    }
}