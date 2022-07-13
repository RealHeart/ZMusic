package me.zhenxin.zmusic.platform

import taboolib.platform.BukkitPlugin
import taboolib.platform.BungeePlugin
import taboolib.platform.VelocityPlugin

/**
 * 获取平台实例
 *
 * @author 真心
 * @since 2021/10/14 15:07
 * @email qgzhenxin@qq.com
 */

/**
 * Bukkit 平台实例
 */
val bukkitPlugin
    get() = BukkitPlugin.getInstance()

/**
 * Bungee 平台实例
 */
val bungeePlugin
    get() = BungeePlugin.getInstance()

/**
 * Velocity 平台实例
 */
val velocityPlugin
    get() = VelocityPlugin.getInstance()
