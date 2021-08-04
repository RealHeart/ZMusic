package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.common.ZMusic
import me.zhenxin.zmusic.common.modules.logger.Logger
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
        me.zhenxin.zmusic.common.logger = Logger(logger)
        MetricsBC(this, 8864)
        proxy.pluginManager.registerCommand(this, CmdExec())
        ZMusic.onEnable(dataFolder)
    }
}
