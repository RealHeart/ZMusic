@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "1.27"
    kotlin("jvm") version "1.5.31"
}

group = "me.zhenxin.zmusic"
version = "3.0-21w38a"

val taboolib = "6.0.3-5"
val minimessage = "4.2.0-SNAPSHOT"
val bungeecord = "1.17-R0.1-SNAPSHOT"
val velocity = "3.0.0"
val hutool = "5.7.13"
val ktorm = "3.4.1"

repositories {
    // 阿里云
    maven("https://maven.aliyun.com/repository/public/")
    // sonatype
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    // Velocity
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
    // ZhenXin
    maven("https://gitee.com/RealHeart/Maven/raw/master")
}

dependencies {
    // NMS
    compileOnly("ink.ptms.core:v11701:11701:mapped")
    compileOnly("ink.ptms.core:v11701:11701:universal")
    compileOnly("ink.ptms:nms-all:1.0.0")

    // Kyori Adventure
    compileOnly("me.zhenxin:adventure-text-minimessage:$minimessage")

    // Platform API
    compileOnly("net.md-5:bungeecord-api:$bungeecord")
    compileOnly("com.velocitypowered:velocity-api:$velocity")

    // hutool
    compileOnly("cn.hutool:hutool-json:$hutool")
    compileOnly("cn.hutool:hutool-http:$hutool")
    compileOnly("cn.hutool:hutool-crypto:$hutool")

    // ktorm
    compileOnly("org.ktorm:ktorm-core:$ktorm")
    compileOnly("org.ktorm:ktorm-support-sqlite:$ktorm")
    compileOnly("org.ktorm:ktorm-support-mysql:$ktorm")

    // Kotlin
    compileOnly(kotlin("stdlib"))
}

taboolib {
    description {
        contributors {
            name("zhen_xin")
            name("BlackNeko")
            name("KanKe")
        }
        links {
            name("homepage").url("https://m.zplu.cc")
        }
        dependencies {
            name("PlaceholderAPI")
                .with("bukkit")
                .optional(true)
        }
    }
    install("common")
    install(
        "platform-bukkit",
        "platform-bungee",
        "platform-velocity"
    )
    install(
        "module-configuration",
        "module-lang",
        "module-metrics",
        "module-nms",
        "module-nms-util"
    )
    // 实验性
    install(
        "expansion-command-helper"
    )
    version = taboolib
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}