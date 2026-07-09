dependencies {
    implementation(project(":zmusic-core"))

    compileOnly(libs.bungeecord)
}

tasks.processResources {
    inputs.property("version", version)
    inputs.property("author", rootProject.extra["zmusicAuthor"])

    filesMatching("bungee.yml") {
        expand(
            mapOf(
                "version" to version,
                "author" to rootProject.extra["zmusicAuthor"]
            )
        )
    }
}
