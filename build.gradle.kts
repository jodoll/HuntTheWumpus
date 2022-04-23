import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "com.johannesdoll.huntthewumpus"
version = "1.0"

val koTestVersion : String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:$koTestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$koTestVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}