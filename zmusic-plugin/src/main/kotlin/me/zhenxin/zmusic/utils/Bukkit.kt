package me.zhenxin.zmusic.utils

import taboolib.platform.BukkitPlugin

/**
 * Bukkit 工具
 *
 * @author 真心
 * @since 2022/8/5 17:44
 * @email qgzhenxin@qq.com
 */

fun bukkitVersion(): String {
    return bukkitPlugin().server.bukkitVersion.split("-")[0]
}

fun nmsVersion(): String {
    val pkgName = bukkitPlugin().server.javaClass.`package`.name
    return pkgName.substring(pkgName.lastIndexOf('.') + 1)
}

private var supportBossBar: Boolean? = null

fun supportBossBar(): Boolean {
    if (supportBossBar == null) {
        val result = VersionCheck("1.9", bukkitVersion()).isLowerThan()
        supportBossBar = result
    }
    return supportBossBar!!
}

private fun bukkitPlugin() = BukkitPlugin.getInstance()
