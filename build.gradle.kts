plugins {
    kotlin("jvm") version "2.4.0"
    `maven-publish`
}

group = "com.cainpvp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testCompileOnly("org.slf4j:slf4j-simple:2.0.17")

    compileOnly("org.slf4j:slf4j-api:2.0.17")
    compileOnly("net.minestom:minestom:2026.05.17-1.21.11")
}

kotlin {
    jvmToolchain(26)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}