import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "com.crowdproj.marketplace"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    tasks.withType<KotlinCompile> {
        val jvmTarget: String by project
        kotlinOptions.jvmTarget = jvmTarget
    }
}
