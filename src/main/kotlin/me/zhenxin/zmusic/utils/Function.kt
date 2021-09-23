package me.zhenxin.zmusic.utils

import cn.hutool.crypto.SecureUtil
import me.zhenxin.adventure.text.Component
import me.zhenxin.adventure.text.minimessage.MiniMessage
import me.zhenxin.zmusic.module.Config
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.impl.*
import java.util.*


/**
 * 扩展函数
 * @author 真心
 * @since 2021/1/23 16:44
 * @email qgzhenxin@qq.com
 */

/**
 * 生成md5
 */
fun String.md5(): String = SecureUtil.md5(this)

/**
 * 替换&为§
 */
fun String.colored(): String = replace("&", "§")


/**
 * 替换&为§
 */
fun List<String>.colored(): List<String> = map { it.replace("&", "§") }

/**
 * 通过 MiniMessage 生成 Component
 */
fun String.component(): Component = MiniMessage.get().parse(this)

/**
 * 通过代号获取相应API实例
 */
fun String.asMusicApi(): MusicApi {
    return when (this) {
        "netease" -> NeteaseApi() // 网易云音乐
        "qq" -> QQMusicApi() // QQ音乐
        "bilibili" -> BiliBiliApi() // 哔哩哔哩
        "kugou" -> KuGouApi() // 酷狗
        "xima" -> XimaApi() // 喜马拉雅
        "soundcloud" -> SoundCloudApi() // SoundCloud
        else -> NeteaseApi() // 理论上永远不会执行
    }
}

fun setLocale() {
    try {
        val lang = Config.LANGUAGE.split("_")
        Locale.setDefault(Locale(lang[0], lang[1]))
    } catch (e: Exception) {
        if (Config.DEBUG) e.printStackTrace()
    }
}