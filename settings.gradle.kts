rootProject.name = "zmusic-plugin-v5"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

include(
    "zmusic-runtime",
    "zmusic-core",
    "zmusic-bukkit",
    "zmusic-bungee",
    "zmusic-velocity"
)
