@file:Suppress("SpellCheckingInspection")

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

version = "3.2.0"

dependencies {
    implementation(project(":zmusic-nms:zmusic-nms-core"))
    implementation(project(":zmusic-nms:zmusic-nms-legacy"))
    implementation(project(":zmusic-nms:zmusic-nms-1.17"))
    implementation(project(":zmusic-nms:zmusic-nms-1.18"))
    implementation(project(":zmusic-nms:zmusic-nms-1.19"))
    implementation(project(":zmusic-nms:zmusic-nms-1.20"))

    compileOnly(libs.spigot)

    compileOnly(libs.placeholderapi)
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("plugin.yml") {
        expand(mapOf("version" to version))
    }
}

tasks.jar {
    archiveBaseName.set("ZMusicBridge")
}

tasks.build {
    dependsOn("shadowJar")
}
