repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":zmusic-core"))
    compileOnly(libs.velocity)
    implementation(libs.bstats.velocity)
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("velocity-plugin.json") {
        expand(mapOf("version" to version))
    }
}