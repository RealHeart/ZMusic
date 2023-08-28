repositories {
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://repo.tabooproject.org/repository/releases")
}

dependencies {
    api(project(":zmusic-common"))

    compileOnly(libs.spigot)
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("plugin.yml") {
        expand(mapOf("version" to version))
    }
}