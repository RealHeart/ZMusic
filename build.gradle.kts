import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "1.22"
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

group = "me.zhenxin.zmusic"
version = "3.0-21w32a"

repositories {
    // 阿里云
    maven("https://maven.aliyun.com/repository/public/")
    // SpigotMC
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    // Velocity
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
    mavenLocal()
}

@Suppress("SpellCheckingInspection")
dependencies {
    compileOnly("ink.ptms.core:v11701:11701:mapped")
    compileOnly("ink.ptms.core:v11701:11701:universal")

    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.17-R0.1-SNAPSHOT")
    compileOnly("com.velocitypowered:velocity-api:3.0.0")
    compileOnly("org.spongepowered:spongeapi:7.2.0")

    compileOnly("com.alibaba:fastjson:1.2.76")
    compileOnly("cn.hutool:hutool-http:5.7.9")

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
        "platform-velocity",
        "platform-sponge-api7"
    )
    install(
        "module-chat",
        "module-configuration",
        "module-lang",
        "module-metrics"
    )
    version = "6.0.0-pre57"
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