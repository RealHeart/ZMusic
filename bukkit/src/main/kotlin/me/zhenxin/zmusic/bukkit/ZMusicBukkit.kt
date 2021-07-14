package me.zhenxin.zmusic.bukkit

import me.zhenxin.zmusic.common.ZMusic
import org.bukkit.plugin.java.JavaPlugin

/**
 * ZMusic Bukkit 主入口
 *
 * @author 真心
 * @since 2021/7/5 13:43
 * @email qgzhenxin@qq.com
 */
class ZMusicBukkit : JavaPlugin() {
    override fun onEnable() {
        ZMusic.onEnable(dataFolder)
    }
}
