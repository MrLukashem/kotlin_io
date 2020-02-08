/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 */

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
    mavenCentral()
}
buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }
}
plugins {

    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    id("org.jetbrains.kotlin.jvm").version("1.3.21")

    // Apply the application plugin to add support for building a CLI application.
    application

    //id("kotlin-android")
    id("org.jetbrains.kotlin.android.extensions") version "1.3.50"
}

dependencies {
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.jetbrains.kotlin:kotlin-android-extensions")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    testImplementation("io.mockk:mockk:1.9.3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.0")

    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.6.1")
}

application { 
    // Define the main class for the application.
    mainClassName = "kotlin_io.AppKt"
}

tasks.named("run", JavaExec::class).configure {
    args = mutableListOf("client|server")
    standardInput = System.`in`
}
