@file:Suppress("SpellCheckingInspection")

version = "3.0.0-beta2"

plugins {
    id("io.izzel.taboolib") version "1.50"
    kotlin("jvm") version "1.7.21"
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
    compileOnly(libs.bundles.hutool)
    compileOnly(libs.bundles.nashorn)
    compileOnly(libs.netty.buffer)

    testImplementation(libs.bundles.hutool)
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
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    archiveBaseName.set("ZMusic")
}