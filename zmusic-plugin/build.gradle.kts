@file:Suppress("SpellCheckingInspection")

version = "3.0.0-beta4"

plugins {
    id("io.izzel.taboolib") version "1.56"
    kotlin("jvm") version "1.8.0"
}

dependencies {
    libs.bundles.nms.get().forEach {
        compileOnly(
            group = it.module.group,
            name = it.module.name,
            version = it.versionConstraint.toString(),
            classifier = "universal"
        )
    }
    compileOnly(libs.nms.legacy)

    compileOnly(libs.placeholderapi)

    compileOnly(libs.bundles.platform)

    compileOnly(libs.okhttp)
    compileOnly(libs.bundles.fastjson)
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.jar {
    archiveBaseName.set("ZMusic")
}