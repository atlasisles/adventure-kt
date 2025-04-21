plugins {
    id("com.vanniktech.maven.publish") version "0.31.0"
    alias(libs.plugins.kotlin.jvm)
}

allprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    group = "com.atlasisles.adventurekt"
    version = "2.0-SNAPSHOT"

    repositories {
        mavenCentral()
        mavenLocal()
        maven(uri("https://maven.nostal.ink/repository/maven-public"))
    }

    dependencies {
        api(rootProject.libs.kotlin.stdlib)
        api(rootProject.libs.adventure.api)
        api(rootProject.libs.adventure.text.minimessage)
        api(rootProject.libs.adventure.text.serializer.gson)
        api(rootProject.libs.adventure.text.serializer.legacy)
        api(rootProject.libs.adventure.text.serializer.plain)
        api(rootProject.libs.adventure.text.serializer.ansi)
    }

    kotlin {
        jvmToolchain(8)
    }

    tasks.compileJava {
        targetCompatibility = "1.8"
        options.encoding = "UTF-8"
    }
}