package cn.iqianye.mc.zmusic.utils.runtask;

import cn.iqianye.mc.zmusic.ZMusicBC;
import net.md_5.bungee.api.ProxyServer;

public class RunTaskBC implements RunTask {

    @Override
    public void run(Runnable runnable) {

    }

    @Override
    public void runAsync(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(ZMusicBC.plugin, runnable);
    }
}