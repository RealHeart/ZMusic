plugins {
    id("io.izzel.taboolib") version "1.40"
    kotlin("jvm") version "1.6.21"
}

group = "me.zhenxin.zmusic"
version = "3.0.0-22w28b"

val taboolib = "6.0.9-26"
val spigot = "1.18.2-R0.1-SNAPSHOT"
val bungeecord = "1.19-R0.1-SNAPSHOT"
val velocity = "3.0.1"
val hutool = "5.8.4"
val nashorn = "15.4"
val nashornSandbox = "0.2.5"
val netty = "4.1.77.Final"

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

    // hutool
    compileOnly("cn.hutool:hutool-http:$hutool")
    compileOnly("cn.hutool:hutool-json:$hutool")
    // nashorn
    compileOnly("org.openjdk.nashorn:nashorn-core:$nashorn")
    compileOnly("org.javadelight:delight-nashorn-sandbox:$nashornSandbox")
    // nettty
    compileOnly("io.netty:netty-buffer:$netty")

    // test
    testImplementation("cn.hutool:hutool-http:$hutool")
    testImplementation("cn.hutool:hutool-json:$hutool")
}

taboolib {
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
    version = taboolib
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    archiveBaseName.set("ZMusic")
}