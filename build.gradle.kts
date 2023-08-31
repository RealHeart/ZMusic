@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.9.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.kyori.blossom") version "1.3.1"
}

subprojects {
    group = "me.zhenxin"
    version = "4.0.0-dev"
    apply {
        plugin("java")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("com.github.johnrengelman.shadow")
        plugin("net.kyori.blossom")
    }
    configure<JavaPluginExtension> {
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
