package cn.iqianye.mc.zmusic.utils.runnable;

import cn.iqianye.mc.zmusic.ZMusicBC;
import net.md_5.bungee.api.ProxyServer;

public class RunTaskBC implements RunTask {
    @Override
    public void start(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(ZMusicBC.plugin, runnable);
    }
}
