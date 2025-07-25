plugins {
    alias(libs.plugins.kotlin.jvm)
}

allprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    group = "com.atlasisles.adventurekt"
    version = "2.7-SNAPSHOT"

    repositories {
        mavenCentral()
        mavenLocal()
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