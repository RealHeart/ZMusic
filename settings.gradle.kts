rootProject.name = "ZMusic"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

include(
    "zmusic-bukkit",
    "zmusic-bungee",
    "zmusic-core",
    "zmusic-runtime",
    "zmusic-velocity"
)
