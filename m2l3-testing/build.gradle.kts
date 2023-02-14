import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm")
}

dependencies {
    val kotestVersion: String by project
    val jUnitJupiterVersion: String by project
    implementation("io.kotest:kotest-framework-engine:$kotestVersion")
    implementation("io.kotest:kotest-framework-datatest:$kotestVersion")
    implementation("io.kotest:kotest-assertions-core:$kotestVersion")
    implementation("io.kotest:kotest-property:$kotestVersion")
    implementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    implementation("org.junit.jupiter:junit-jupiter-params:$jUnitJupiterVersion")
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        filter {
            isFailOnNoMatchingTests = false
        }
        testLogging {
            showExceptions = true
            showStandardStreams = true
            events = setOf(TestLogEvent.FAILED, TestLogEvent.PASSED)
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}
