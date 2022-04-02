import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    java
}

group = "gay.vaskel"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.dimensional.fun/snapshots")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    // Shared things between modules
    implementation(project(":common"))

    // Kotlin standard stuff
    implementation(kotlin("stdlib"))
    implementation("org.slf4j:slf4j-simple:1.7.29")
    implementation("io.github.microutils:kotlin-logging:1.12.5")
    implementation("io.javalin:javalin:4.2.0")

    // Discord
    implementation("gg.mixtape:flight:2.5-RC.3")
    implementation("gg.mixtape:commons:1.4-RC")
    implementation("net.dv8tion:JDA:5.0.0-alpha.3") {
        exclude("club.minnced","opus-java")
    }

    implementation("com.sksamuel.hoplite:hoplite-core:1.4.16")
    implementation("com.sksamuel.hoplite:hoplite-yaml:1.4.16")

    // Database
    val exposedVersion = "0.37.3"
    implementation("org.postgresql:postgresql:42.2.2")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    // Needed for upsert support
    implementation("pw.forst", "exposed-upsert", "1.1.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("discord-bot-$version-with-deps.jar")
    manifest {
        attributes(mapOf("Main-Class" to "gay.vaskel.bot.MainKt"))
    }
}