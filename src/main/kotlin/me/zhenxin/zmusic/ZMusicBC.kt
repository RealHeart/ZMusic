package me.zhenxin.zmusic

import me.zhenxin.zmusic.bstats.MetricsBC
import me.zhenxin.zmusic.command.CmdBC
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.event.EventBC
import me.zhenxin.zmusic.module.logger.LoggerBC
import me.zhenxin.zmusic.module.tasker.TaskerBC
import me.zhenxin.zmusic.util.VersionCheck
import net.md_5.bungee.api.plugin.Plugin


class ZMusicBC : Plugin() {

    override fun onEnable() {
        proxy.registerChannel("zmusic:channel")
        proxy.registerChannel("allmusic:channel")
        proxy.registerChannel("AudioBuffer")
        proxy.pluginManager.registerCommand(this, CmdBC())
        proxy.pluginManager.registerListener(this, EventBC())

        MetricsBC(this, 8864)

        ZMusic.isBC = true
        ZMusic.logger = LoggerBC(proxy.console)
        ZMusic.thisVer = description.version
        ZMusic.tasker = TaskerBC()
        ZMusic.dataFolder = dataFolder
        val zLibVer = proxy.pluginManager.getPlugin("ZLib")!!.description.version
        if (VersionCheck(ZMusic.zLibVer, zLibVer).isLowerThan()) {
            ZMusic.logger.error(Lang.Loading.zLibNoOK.replace("%version%", ZMusic.zLibVer))
            ZMusic.zLibIsOK = false
        }
        ZMusic.enable()
    }
}
