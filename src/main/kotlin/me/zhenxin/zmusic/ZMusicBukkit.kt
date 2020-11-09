package me.zhenxin.zmusic

import me.zhenxin.zmusic.bstats.MetricsBukkit
import me.zhenxin.zmusic.command.CmdBukkit
import me.zhenxin.zmusic.module.logger.LoggerBukkit
import me.zhenxin.zmusic.module.tasker.TaskerBukkit
import me.zhenxin.zmusic.module.version.VersionBukkit
import org.bukkit.plugin.java.JavaPlugin


class ZMusicBukkit : JavaPlugin() {

    override fun onEnable() {
        getCommand("zm")?.setExecutor(CmdBukkit())
        getCommand("zm")?.tabCompleter = CmdBukkit()

        MetricsBukkit(this, 7291)

        val version = VersionBukkit()
        server.messenger.registerOutgoingPluginChannel(this, "allmusic:channel")
        if (!version.high("1.12")) {
            server.messenger.registerOutgoingPluginChannel(this, "AudioBuffer")
        }
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
        ZMusic.enable()
    }
}
