package me.zhenxin.zmusic

import me.zhenxin.zmusic.bstats.MetricsBC
import me.zhenxin.zmusic.command.CmdBC
import me.zhenxin.zmusic.event.EventBC
import me.zhenxin.zmusic.module.logger.LoggerBC
import me.zhenxin.zmusic.module.tasker.TaskerBC
import net.md_5.bungee.api.plugin.Plugin


class ZMusicBC : Plugin() {

    override fun onEnable() {
        proxy.registerChannel("zmusic:channel")
        proxy.registerChannel("allmusic:channel")
        proxy.registerChannel("AudioBuffer")
        proxy.pluginManager.registerCommand(this, CmdBC())
        proxy.pluginManager.registerListener(this, EventBC())

        MetricsBC(this,8864)

        ZMusic.isBC = true
        ZMusic.logger = LoggerBC(proxy.console)
        ZMusic.thisVer = description.version
        ZMusic.tasker = TaskerBC()
        ZMusic.dataFolder = dataFolder
        ZMusic.enable()
    }
}
