package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.common.ZMusic
import net.md_5.bungee.api.plugin.Plugin

/**
 * ZMusic Bungeecord 主入口
 *
 * @author 真心
 * @since 2021/7/8 13:56
 * @email qgzhenxin@qq.com
 */
class ZMusicBungee : Plugin() {
    override fun onEnable() {
        ZMusic.onEnable(dataFolder)
    }
}
