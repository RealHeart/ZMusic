dependencies {
    implementation(project(":zmusic-core"))

    compileOnly(libs.spigot)
    compileOnly(libs.placeholderapi)
}

tasks.processResources {
    inputs.property("version", version)
    inputs.property("author", rootProject.extra["zmusicAuthor"])

    filesMatching("plugin.yml") {
        expand(
            mapOf(
                "version" to version,
                "author" to rootProject.extra["zmusicAuthor"]
            )
        )
    }
}
