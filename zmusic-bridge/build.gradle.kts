group = "me.zhenxin"
version = "3.0.0"

repositories {
    // TabooLib
    maven("https://repo.tabooproject.org/repository/releases")
}

dependencies {
    compileOnly("org.jetbrains:annotations:20.1.0")
    // NMS
    compileOnly("ink.ptms.core:v11900:11900:universal")
    compileOnly("ink.ptms.core:v11802:11802:universal")
    compileOnly("ink.ptms.core:v11801:11801:universal")
    compileOnly("ink.ptms.core:v11701:11701:universal")
    compileOnly("ink.ptms:nms-all:1.0.0") // 1.8_R1 - 1.16_R3

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")

    // PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.2")
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

