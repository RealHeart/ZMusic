package me.zhenxin.zmusic

import me.zhenxin.zmusic.module.logger.LoggerBC
import net.md_5.bungee.api.plugin.Plugin

class ZMusicBC : Plugin() {

    override fun onEnable() {
        ZMusic.isBC = true
        ZMusic.plugin = this
        ZMusic.logger = LoggerBC(proxy.console)
        ZMusic.thisVer = description.version
//        ZMusic.tasker = RunTaskBC()
//        ZMusic.msg = MessageBC()
//        ZMusic.sender = SendBC()
//        ZMusic.player = PlayerBC()
        ZMusic.dataFolder = dataFolder

        ZMusic.enable()
    }
}
