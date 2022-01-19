@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "1.34"
    kotlin("jvm") version "1.6.10"
}

group = "me.zhenxin.zmusic"
version = "3.0-22w04a"

val taboolib = "6.0.7-19"
val minimessage = "4.10.0-SNAPSHOT"
val bungeecord = "1.18-R0.1-SNAPSHOT"
val velocity = "3.0.0"
val okhttp = "4.9.3"
val fastjson = "1.2.79"

repositories {
    // 阿里云
    maven("https://maven.aliyun.com/repository/public/")
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

    // okhttp
    compileOnly("com.squareup.okhttp3:okhttp:$okhttp")
    // fastjson
    compileOnly("com.alibaba:fastjson:$fastjson")

    compileOnly("io.netty:netty-buffer:4.1.72.Final")
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
        "module-metrics",
        "module-nms",
        "module-nms-util"
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
    targetCompatibility = JavaVersion.VERSION_1_8
}