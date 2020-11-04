package me.zhenxin.zmusic.module.version

import org.bukkit.Bukkit

class VersionBukkit {
    private val rawVersion: String = Bukkit.getBukkitVersion()
    private val version: String = rawVersion.split("-")[0]

    //比目标版本低
    fun isLowerThan(ver: String): Boolean {
        val thisVersions: List<String> = version.split("\\.")
        val compareVersions = ver.split("\\.")
        for (i in compareVersions.indices) {
            if (thisVersions[i].toInt() > thisVersions[i].toInt()) {
                return true
            }
        }
        return false
    }

    //比目标版本高
    fun isHigherThan(ver: String): Boolean {
        val thisVersions: List<String> = version.split("\\.")
        val compareVersions = ver.split("\\.")
        for (i in compareVersions.indices) {
            if (compareVersions[i].toInt() < thisVersions[i].toInt()) {
                return true
            }
        }
        return false
    }

    //与目标版本相等
    fun isEquals(ver: String): Boolean = version == ver
}
