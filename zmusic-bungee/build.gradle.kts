repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":zmusic-common"))
    compileOnly(libs.bungeecord)
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("bungee.yml") {
        expand(mapOf("version" to version))
    }
}