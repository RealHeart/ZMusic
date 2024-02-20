@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun gitCommitHash(): String {
    val builder = ProcessBuilder("git", "rev-parse", "--short", "HEAD")
    val process = builder.start()
    val reader = process.inputReader()
    val hash = reader.readText().trim()
    return if (hash.isNotEmpty()) ".$hash" else ""
}

plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.johnrengelman.shadow)
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "me.zhenxin"
    version = "4.0.0-dev" + gitCommitHash()

    apply {
        plugin("java")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("com.github.johnrengelman.shadow")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.build {
        dependsOn(tasks.shadowJar)
    }

    tasks.shadowJar {
        exclude("kotlin/**")
        exclude("org/jetbrains/**")
        exclude("org/intellij/**")

        relocate("kotlin", "me.zhenxin.zmusic.library.kotlin")
        relocate("me.lucko", "me.zhenxin.zmusic.library")
        relocate("org.objectweb", "me.zhenxin.zmusic.library")
        relocate("org.bstats", "me.zhenxin.zmusic.library.bstats")

        archiveClassifier.set("")
        minimize()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

tasks.jar { enabled = false }
