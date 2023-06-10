package me.zhenxin.zmusic.utils.runtask;

import me.zhenxin.zmusic.ZMusicBukkit;
import org.bukkit.Bukkit;

public class RunTaskBukkit implements RunTask {

    @Override
    public void run(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTask(ZMusicBukkit.plugin, runnable);
    }

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(ZMusicBukkit.plugin, runnable);
    }
}
