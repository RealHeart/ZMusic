package me.zhenxin.zmusic.utils

import me.zhenxin.zmusic.status.removeBossBar
import me.zhenxin.zmusic.module.sendPluginMessage
import taboolib.common.platform.ProxyPlayer

/**
 * Mod通信相关函数
 *
 * @author 真心
 * @since 2021/9/8 10:24
 * @email qgzhenxin@qq.com
 */

/**
 * 播放音乐
 * @param url 播放链接
 */
fun ProxyPlayer.playMusic(url: String) {
    sendPluginMessage("[Play]$url")
}

fun ProxyPlayer.stopMusic() {
    sendPluginMessage("[Stop]")
    removeBossBar()
}