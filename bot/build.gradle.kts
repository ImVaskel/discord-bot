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

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}