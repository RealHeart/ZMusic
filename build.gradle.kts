@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

subprojects {
    group = "me.zhenxin"
    version = "4.0.0-dev"
    apply {
        plugin("java")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("com.github.johnrengelman.shadow")
    }
    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.processResources {
        inputs.property("version", version)

        filesMatching("plugin.yml") {
            expand(mapOf("version" to version))
        }

        filesMatching("bungee.yml") {
            expand(mapOf("version" to version))
        }

        filesMatching("velocity-plugin.json") {
            expand(mapOf("version" to version))
        }
    }

    tasks.build {
        dependsOn(tasks.shadowJar)
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

tasks.jar { enabled = false }
