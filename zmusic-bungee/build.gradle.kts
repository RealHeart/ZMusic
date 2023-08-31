repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":zmusic-common"))

    compileOnly(libs.bungeecord)
    compileOnly(libs.bstats.bungeecord)
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("bungee.yml") {
        expand(mapOf("version" to version))
    }
}

blossom {
    val constants = "src/main/java/me/zhenxin/zmusic/ZMusicBungee.java"
    replaceToken("#BSTATS_VERSION#", libs.versions.bstats.get(), constants)
}
