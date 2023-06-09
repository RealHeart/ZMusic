@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = "3.0.0-beta8"

plugins {
    id("io.izzel.taboolib") version "1.56"
    kotlin("jvm") version "1.8.21"
}

dependencies {
    taboo(project(":zmusic-nms:zmusic-nms-core"))
    taboo(project(":zmusic-nms:zmusic-nms-legacy"))
    taboo(project(":zmusic-nms:zmusic-nms-1.17"))
    taboo(project(":zmusic-nms:zmusic-nms-1.18"))
    taboo(project(":zmusic-nms:zmusic-nms-1.19"))
    taboo(project(":zmusic-nms:zmusic-nms-1.20"))

    compileOnly(libs.placeholderapi)

    compileOnly(libs.bundles.platform)

    compileOnly(libs.bundles.hutool)
    compileOnly(libs.bundles.nashorn)
    compileOnly(libs.netty.buffer)
}

taboolib {
    group = "me.zhenxin.zmusic"
    description {
        name("ZMusic")
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
    version = libs.versions.taboolib.get()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    archiveBaseName.set("ZMusic")
}