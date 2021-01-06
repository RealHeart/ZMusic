import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20" // Kotlin JVM
}
group = "me.zhenxin"
version = "3.0"

repositories {
    mavenLocal()
    maven {
        name = "AliyunJCenter"
        setUrl("https://maven.aliyun.com/repository/public/")
    }
    maven {
        name = "Spigot"
        setUrl("https://hub.spigotmc.org/nexus/content/groups/public/")
    }
    maven {
        name = "PlaceholderAPI"
        setUrl("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        name = "jitpack.io"
        setUrl("https://jitpack.io/")
    }
    maven {
        name = "ZhenXin"
        setUrl("https://realheart.gitee.io/maven/")
    }
    maven {
        name = "ViaVersion"
        setUrl("https://repo.viaversion.com/")
    }
}
dependencies {

    compileOnly("org.spigotmc", "spigot-api", "1.16.4-R0.1-SNAPSHOT") // Spigot API
    testImplementation("com.google.code.gson", "gson", "2.8.0") // Gson - 测试环境
    compileOnly("net.md-5", "bungeecord-api", "1.16-R0.4-SNAPSHOT") // BC API

    compileOnly("com.alibaba", "fastjson", "1.2.73") // fastjson

    // NMS
    val nms = "1.0"
    compileOnly("me.zhenxin.mc", "nms-1.8-R3", nms)
    compileOnly("me.zhenxin.mc", "nms-1.12-R1", nms)
    compileOnly("me.zhenxin.mc", "nms-1.13-R2", nms)
    compileOnly("me.zhenxin.mc", "nms-1.14-R1", nms)
    compileOnly("me.zhenxin.mc", "nms-1.15-R1", nms)
    compileOnly("me.zhenxin.mc", "nms-1.16-R1", nms)
    compileOnly("me.zhenxin.mc", "nms-1.16-R2", nms)
    compileOnly("me.zhenxin.mc", "nms-1.16-R3", nms)

    compileOnly("me.clip", "placeholderapi", "2.9.2") // PAPI
    compileOnly("com.github.MilkBowl", "VaultAPI", "1.7") // Vault
    compileOnly("us.myles", "viaversion", "3.1.0") // ViaVersion
    compileOnly("io.netty", "netty-buffer", "4.1.51.Final") // netty-buffer
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.processResources {
    // 替换版本
    from("src/main/resources/bungee.yml") {
        // BC插件版本
        filter { return@filter it.replace("\${version}", version.toString()) }
    }

    from("src/main/resources/plugin.yml") {
        // Bukkit插件版本
        filter { return@filter it.replace("\${version}", version.toString()) }
    }
}

