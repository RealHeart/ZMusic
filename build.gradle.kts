@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "1.40"
    kotlin("jvm") version "1.6.21"
}

group = "me.zhenxin.zmusic"
version = "3.0.0-22w28a"

val taboolib = "6.0.9-14"
val spigot = "1.18.2-R0.1-SNAPSHOT"
val bungeecord = "1.19-R0.1-SNAPSHOT"
val velocity = "3.0.1"
val okhttp = "4.10.0"
val fastjson = "2.0.8"
val nashorn = "15.4"
val nashornSandbox = "0.2.5"
val netty = "4.1.77.Final"

repositories {
    // spigot
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    // bungeecord
    maven("https://oss.sonatype.org/content/repositories/snapshots")
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

    // Platform API
    compileOnly("net.md-5:bungeecord-api:$bungeecord")
    compileOnly("com.velocitypowered:velocity-api:$velocity")

    // okhttp
    compileOnly("com.squareup.okhttp3:okhttp:$okhttp")
    testImplementation("com.squareup.okhttp3:okhttp:$okhttp")
    // fastjson
    compileOnly("com.alibaba.fastjson2:fastjson2-kotlin:$fastjson")
    testImplementation("com.alibaba.fastjson2:fastjson2-kotlin:$fastjson")
    // nashorn
    compileOnly("org.openjdk.nashorn:nashorn-core:$nashorn")
    compileOnly("org.javadelight:delight-nashorn-sandbox:$nashornSandbox")
    // nettty
    compileOnly("io.netty:netty-buffer:$netty")
}

taboolib {
    description {
        contributors {
            name("ZhenXin")
            name("BlackNeko")
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
    targetCompatibility = JavaVersion.VERSION_17
}