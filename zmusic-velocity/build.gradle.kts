repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":zmusic-common"))
    compileOnly(libs.velocity)
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("velocity-plugin.json") {
        expand(mapOf("version" to version))
    }
}