package me.zhenxin.zmusic.bukkit

import me.zhenxin.zmusic.common.ZMusic
import me.zhenxin.zmusic.common.modules.logger.Logger
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
        me.zhenxin.zmusic.common.logger = Logger(logger)
        MetricsBukkit(this, 7291)
        getCommand("zm")?.setExecutor(CmdExec())
        getCommand("zm")?.tabCompleter = CmdExec()
        ZMusic.onEnable(dataFolder)
    }
}
