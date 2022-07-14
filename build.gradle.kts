plugins {
    java
}

allprojects {
    repositories {
        // spigot
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        // bungeecord
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        // velocity
        maven("https://nexus.velocitypowered.com/repository/maven-public/")
        // PlaceholderAPI
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi")
        mavenCentral()
    }
}

subprojects {
    group = "me.zhenxin"
    version = "3.0.0-22w28b"
    apply {
        plugin("java")
    }
    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

tasks.jar { enabled = false }
