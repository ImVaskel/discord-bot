plugins {
    kotlin("jvm") version "1.5.31"
    java
}

group = "gay.vaskel"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    val exposedVersion = "0.37.3"
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")


    implementation("com.sksamuel.hoplite:hoplite-core:1.4.16")

    // Kotlin standard stufs
    implementation(kotlin("stdlib"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}
