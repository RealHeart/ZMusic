repositories {
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://repo.tabooproject.org/repository/releases")
}

dependencies {
    api(project(":zmusic-common"))
    compileOnly("org.spigotmc", "spigot-api", "1.19.4-R0.1-SNAPSHOT")

    // NMS
    compileOnly("ink.ptms.core:v11904:11904:universal")
    compileOnly("ink.ptms.core:v11903:11903:universal")
    compileOnly("ink.ptms.core:v11900:11900:universal")
    compileOnly("ink.ptms.core:v11802:11802:universal")
    compileOnly("ink.ptms.core:v11801:11801:universal")
    compileOnly("ink.ptms.core:v11701:11701:universal")
    compileOnly("ink.ptms:nms-all:1.0.0") // 1.8_R1 - 1.16_R3
}
