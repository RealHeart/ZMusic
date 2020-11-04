package me.zhenxin.zmusic.module.tasker

import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.ZMusicBC
import me.zhenxin.zmusic.module.Tasker
import net.md_5.bungee.api.ProxyServer

class TaskerBC : Tasker {

    override fun async(runnable: Runnable) {
        ProxyServer.getInstance().scheduler.runAsync(ZMusic.plugin as ZMusicBC, runnable)
    }
}
