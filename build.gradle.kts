@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "1.26"
    kotlin("jvm") version "1.5.30"
}

group = "me.zhenxin.zmusic"
version = "3.0-21w35b"

repositories {
    // 阿里云
    maven("https://maven.aliyun.com/repository/public/")
    // sonatype
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    // Velocity
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
    // ZhenXin
    maven("https://gitee.com/RealHeart/Maven/raw/master")
    mavenLocal()
}

dependencies {
    // TabooLib
    compileOnly("ink.ptms.core:v11701:11701:mapped")
    compileOnly("ink.ptms.core:v11701:11701:universal")

    // Kyori Adventure
    compileOnly("me.zhenxin:adventure-text-minimessage:4.1.0-SNAPSHOT")

    // Platform API
    compileOnly("net.md-5:bungeecord-api:1.17-R0.1-SNAPSHOT")
    compileOnly("com.velocitypowered:velocity-api:3.0.0")
    compileOnly("org.spongepowered:spongeapi:7.2.0")

    // Runtime
    compileOnly("com.alibaba:fastjson:1.2.76")
    compileOnly("cn.hutool:hutool-http:5.7.9")
    compileOnly("cn.hutool:hutool-crypto:5.7.9")

    // Kotlin
    compileOnly(kotlin("stdlib"))
}

taboolib {
    description {
        contributors {
            name("真心")
        }
        description {
            desc("全功能Minecraft点歌插件")
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
        "module-metrics"
    )
    version = "6.0.0-44"
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