package me.zhenxin.zmusic.module.tasker

import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.ZMusicBukkit
import me.zhenxin.zmusic.module.Tasker
import org.bukkit.Bukkit

class TaskerBukkit : Tasker {

    override fun async(runnable: Runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(ZMusic.plugin as ZMusicBukkit, runnable)
    }
}
