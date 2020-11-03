import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}
group = "me.zhenxin"
version = "3.0"

repositories {
    mavenLocal()
    maven {
        name = "AliyunJCenter"
        setUrl("https://maven.aliyun.com/repository/public")
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
        setUrl("https://jitpack.io")
    }
    maven {
        name = "ZhenXin"
        setUrl("https://code.aliyun.com/zhenxin/Maven/raw/master/")
    }
    maven {
        name = "ViaVersion"
        setUrl("https://repo.viaversion.com/")
    }
    jcenter()
}
dependencies {
    // Kotlin
    compileOnly("org.spigotmc", "spigot-api", "1.16.3-R0.1-SNAPSHOT")
    compileOnly("net.md-5", "bungeecord-api", "1.16-R0.4-SNAPSHOT")
    compileOnly("org.bukkit.nms", "v1_8_R3", "1")
    compileOnly("org.bukkit.nms", "v1_12_R1", "1")
    compileOnly("org.bukkit.nms", "v1_13_R2", "1")
    compileOnly("org.bukkit.nms", "v1_14_R1", "1")
    compileOnly("org.bukkit.nms", "v1_15_R1", "1")
    compileOnly("org.bukkit.nms", "v1_16_R1", "1")
    compileOnly("org.bukkit.nms", "v1_16_R2", "1")
    compileOnly("me.clip", "placeholderapi", "2.9.2")
    compileOnly("com.github.MilkBowl", "VaultAPI", "1.7")
    compileOnly("us.myles", "viaversion", "3.1.0")
    compileOnly("io.netty", "netty-buffer", "4.1.51.Final")
    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}


tasks.build {
    dependsOn(tasks.shadowJar)
}

