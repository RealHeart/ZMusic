package me.zhenxin.zmusic

import org.bukkit.plugin.java.JavaPlugin

class ZMusicBukkit : JavaPlugin() {
    var plugin: JavaPlugin? = this

    override fun onEnable() {
        if (server.pluginManager.isPluginEnabled("AudioBuffer")) {
            ZMusic.logger?.error("请勿安装AudioBuffer插件.")
            ZMusic.isEnable = false;
        }
        if (server.pluginManager.isPluginEnabled("AllMusic")) {
            ZMusic.logger?.error("请勿安装AllMusic插件.")
            ZMusic.isEnable = false
        }
    }
}
