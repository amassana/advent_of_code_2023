import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
    application
    id("com.diffplug.spotless") version "6.23.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

spotless {
    kotlin {
        targetExclude("**/build/**")
        ktlint("1.0.1")
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint("1.0.1")
    }
}
