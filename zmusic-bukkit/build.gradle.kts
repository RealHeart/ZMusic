repositories {
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://repo.tabooproject.org/repository/releases")
}

dependencies {
    api(project(":zmusic-core"))

    compileOnly(libs.spigot)
    compileOnly(libs.bstats.bukkit)
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("plugin.yml") {
        expand(mapOf("version" to version))
    }
}
