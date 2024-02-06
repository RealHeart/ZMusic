package me.zhenxin.zmusic;

import me.zhenxin.zmusic.bstats.MetricsBC;
import me.zhenxin.zmusic.command.CmdBC;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.event.EventBC;
import me.zhenxin.zmusic.utils.CookieUtils;
import me.zhenxin.zmusic.utils.log.LogBC;
import me.zhenxin.zmusic.utils.message.MessageBC;
import me.zhenxin.zmusic.utils.mod.SendBC;
import me.zhenxin.zmusic.utils.music.Music;
import me.zhenxin.zmusic.utils.player.PlayerBC;
import me.zhenxin.zmusic.utils.runtask.RunTaskBC;
import net.md_5.bungee.api.plugin.Plugin;

public class ZMusicBC extends Plugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        ZMusic.log = new LogBC(getProxy().getConsole());
        plugin = this;
        ZMusic.isBC = true;
        ZMusic.runTask = new RunTaskBC();
        ZMusic.message = new MessageBC();
        ZMusic.music = new Music();
        ZMusic.send = new SendBC();
        ZMusic.player = new PlayerBC();
        ZMusic.dataFolder = getDataFolder();
        if (!ZMusic.dataFolder.exists()) {
            ZMusic.dataFolder.mkdir();
        }
        Config.debug = true;
        ZMusic.thisVer = getDescription().getVersion();
        ZMusic.log.sendNormalMessage("正在加载中....");
        CookieUtils.initCookieManager();
        new MetricsBC(this, 8864);
        getProxy().registerChannel("zmusic:channel");
        getProxy().registerChannel("allmusic:channel");
        getProxy().registerChannel("AudioBuffer");
        getProxy().getPluginManager().registerCommand(this, new CmdBC());
        getProxy().getPluginManager().registerListener(this, new EventBC());
        ZMusic.loadEnd(getProxy().getConsole());
    }

    @Override
    public void onDisable() {
        ZMusic.disable();
    }

}
