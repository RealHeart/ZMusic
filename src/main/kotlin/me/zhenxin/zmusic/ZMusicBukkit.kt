package me.zhenxin.zmusic

import me.zhenxin.zmusic.bstats.MetricsBukkit
import me.zhenxin.zmusic.command.CmdBukkit
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.module.logger.LoggerBukkit
import me.zhenxin.zmusic.module.tasker.TaskerBukkit
import me.zhenxin.zmusic.util.VersionCheck
import org.bukkit.plugin.java.JavaPlugin


class ZMusicBukkit : JavaPlugin() {

    override fun onEnable() {
        ZMusic.plugin = this
        getCommand("zm")?.setExecutor(CmdBukkit())
        getCommand("zm")?.tabCompleter = CmdBukkit()

        MetricsBukkit(this, 7291)

        ZMusic.isBC = true
        ZMusic.logger = LoggerBukkit(server.consoleSender)
        ZMusic.thisVer = description.version
        ZMusic.tasker = TaskerBukkit()
        ZMusic.dataFolder = dataFolder
        if (server.pluginManager.isPluginEnabled("AudioBuffer")) {
            ZMusic.isEnable = false
        }
        if (server.pluginManager.isPluginEnabled("AllMusic")) {
            ZMusic.isEnable = false
        }
        val zLibVer = server.pluginManager.getPlugin("ZLib")!!.description.version
        if (VersionCheck(ZMusic.zLibVer, zLibVer).isLowerThan()) {
            ZMusic.logger.error(Lang.Loading.zLibNoOK.replace("%version%", ZMusic.zLibVer))
            ZMusic.zLibIsOK = false
        }
        server.messenger.registerOutgoingPluginChannel(this, "allmusic:channel")
        if (!VersionCheck("1.12", server.bukkitVersion).isHigherThan()) {
            server.messenger.registerOutgoingPluginChannel(this, "AudioBuffer")
        }
        ZMusic.enable()
    }
}
