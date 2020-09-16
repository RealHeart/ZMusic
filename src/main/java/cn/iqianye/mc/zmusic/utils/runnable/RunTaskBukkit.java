package cn.iqianye.mc.zmusic.utils.runnable;

import cn.iqianye.mc.zmusic.ZMusicBukkit;
import org.bukkit.Bukkit;

public class RunTaskBukkit implements RunTask {

    @Override
    public void start(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(ZMusicBukkit.plugin, runnable);
    }
}
