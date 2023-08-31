repositories {
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://repo.tabooproject.org/repository/releases")
}

dependencies {
    api(project(":zmusic-common"))

    compileOnly(libs.spigot)
    compileOnly(libs.bstats.bukkit)
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("plugin.yml") {
        expand(mapOf("version" to version))
    }
}

blossom {
    val constants = "src/main/java/me/zhenxin/zmusic/ZMusicBukkit.java"
    replaceToken("#BSTATS_VERSION#", libs.versions.bstats.get(), constants)
}
