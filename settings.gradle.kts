rootProject.name = "ZMusic"

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
include("zmusic-plugin")
include("zmusic-bridge")

// NMS
include(
    "zmusic-nms",
    "zmusic-nms:zmusic-nms-core",
    "zmusic-nms:zmusic-nms-legacy",
    "zmusic-nms:zmusic-nms-1.17",
    "zmusic-nms:zmusic-nms-1.18",
    "zmusic-nms:zmusic-nms-1.19",
    "zmusic-nms:zmusic-nms-1.20"
)
