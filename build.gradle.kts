@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "1.40"
    kotlin("jvm") version "1.6.20"
}

group = "me.zhenxin.zmusic"
version = "3.0.0-22w18a"

val taboolib = "6.0.8-9"
val minimessage = "4.10.0-SNAPSHOT"
val spigot = "1.19-R0.1-SNAPSHOT"
val bungeecord = "1.19-R0.1-SNAPSHOT"
val velocity = "3.0.1"
val okhttp = "4.9.3"
val fastjson = "2.0.3"
val netty = "4.1.77.Final"

repositories {
    // bungeecord
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
    // Velocity
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
    // ZhenXin
    maven("https://gitee.com/RealHeart/Maven/raw/master")
    mavenCentral()
}

dependencies {
    // NMS
    compileOnly("ink.ptms.core:v11900:11900:universal")
    compileOnly("ink.ptms.core:v11802:11802:universal")
    compileOnly("ink.ptms.core:v11801:11801:universal")
    compileOnly("ink.ptms.core:v11701:11701:universal")
    compileOnly("ink.ptms:nms-all:1.0.0") // 1.8_R1 - 1.16_R3

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:$spigot")

    // Kyori Adventure
    compileOnly("me.zhenxin:adventure-text-minimessage:$minimessage")

    // Platform API
    compileOnly("net.md-5:bungeecord-api:$bungeecord")
    compileOnly("com.velocitypowered:velocity-api:$velocity")

    // okhttp
    compileOnly("com.squareup.okhttp3:okhttp:$okhttp")
    // fastjson
    compileOnly("com.alibaba.fastjson2:fastjson2:$fastjson")

    compileOnly("io.netty:netty-buffer:$netty")
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
    // 公共
    install("common", "common-5")
    // 平台
    install(
        "platform-bukkit",
        "platform-bungee",
        "platform-velocity"
    )
    // 模块
    install(
        "module-configuration",
        "module-lang",
        "module-metrics"
    )
    // 扩展
    install(
        "expansion-command-helper",
        "expansion-javascript"
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
    targetCompatibility = JavaVersion.VERSION_17
}