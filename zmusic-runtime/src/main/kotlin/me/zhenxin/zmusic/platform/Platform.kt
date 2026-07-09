package me.zhenxin.zmusic.platform

/**
 * ZMusic 支持的平台类型。
 *
 * @property displayName 展示名称
 * @author 真心
 * @since 5.0.0
 */
enum class Platform(val displayName: String) {
    BUKKIT("Bukkit"),
    BUNGEE("BungeeCord"),
    VELOCITY("Velocity")
}
