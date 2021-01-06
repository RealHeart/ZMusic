package me.zhenxin.zmusic.util

/**
 * @author 真心
 * @date 2021/1/6 22:12
 * @email qgzhenxin@qq.com
 * @description: 版本检测类
 * @param targetVersion 目标版本
 * @param localVersion 本地版本
 */
class VersionCheck(private val targetVersion: String, private val localVersion: String) {

    private val localVerInt = ("1" + localVersion
        .replace("[^0-9.]".toRegex(), "")
        .replace(".", "")).toInt()
    private val targetVerInt = ("1" + targetVersion
        .replace("[^0-9.]".toRegex(), "")
        .replace(".", "")).toInt()

    //比目标版本低
    fun isLowerThan(): Boolean {
        if (localVerInt < targetVerInt) {
            return true
        }
        return equals()
    }


    //比目标版本高
    fun isHigherThan(): Boolean {
        println(localVerInt)
        println(targetVerInt)
        if (localVerInt > targetVerInt) {
            return true
        }
        return equals()
    }

    //与目标版本相等
    fun equals() = localVersion == targetVersion
}
