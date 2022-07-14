repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":zmusic-common"))
    compileOnly("net.md-5", "bungeecord-api", "1.19-R0.1-SNAPSHOT")
}