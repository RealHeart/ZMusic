package me.zhenxin.zmusic.module.version

import org.bukkit.Bukkit

class VersionBukkit {
    private val rawVersion: String = Bukkit.getBukkitVersion()
    private val version: String = rawVersion.split("-")[0]

    //比目标版本低
    fun low(ver: String): Boolean {
        val thisVersions = version.split(".")
        val compareVersions = ver.split(".")
        compareVersions.forEachIndexed { index, s ->
            if (s.toInt() > thisVersions[index].toInt()) {
                return true
            }
        }
        return false
    }

    //比目标版本高
    fun high(ver: String): Boolean {
        val thisVersions = version.split(".")
        val compareVersions = ver.split(".")
        compareVersions.forEachIndexed { index, s ->
            if (s.toInt() < thisVersions[index].toInt()) {
                return true
            }
        }
        return false
    }
}
