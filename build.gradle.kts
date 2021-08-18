import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "1.18"
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
}

group = "me.zhenxin.zmusic"
version = "3.0-21w32a"

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
    version = "6.0.0-pre51"
}

repositories {
    // 阿里云
    maven("https://maven.aliyun.com/repository/public/")
    // SpigotMC
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    mavenLocal()
}

dependencies {
    compileOnly("ink.ptms.core:v11701:11701:mapped")
    compileOnly("ink.ptms.core:v11701:11701:universal")

    compileOnly("net.md-5:bungeecord-api:1.17-R0.1-SNAPSHOT")

    compileOnly("com.alibaba:fastjson:1.2.76")
    compileOnly("com.squareup.okhttp3:okhttp:4.9.1")

    compileOnly(kotlin("stdlib"))
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