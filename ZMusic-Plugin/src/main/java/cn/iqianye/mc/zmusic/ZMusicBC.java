package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.bstats.MetricsBC;
import cn.iqianye.mc.zmusic.command.CmdBC;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.event.EventBC;
import cn.iqianye.mc.zmusic.utils.log.LogBC;
import cn.iqianye.mc.zmusic.utils.message.MessageBC;
import cn.iqianye.mc.zmusic.utils.mod.SendBC;
import cn.iqianye.mc.zmusic.utils.music.MusicBC;
import cn.iqianye.mc.zmusic.utils.player.PlayerBC;
import cn.iqianye.mc.zmusic.utils.runtask.RunTaskBC;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.CookieHandler;
import java.net.CookieManager;

public class ZMusicBC extends Plugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        ZMusic.log = new LogBC(getProxy().getConsole());
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
        plugin = this;
        ZMusic.isBC = true;
        ZMusic.runTask = new RunTaskBC();
        ZMusic.message = new MessageBC();
        ZMusic.music = new MusicBC();
        ZMusic.send = new SendBC();
        ZMusic.player = new PlayerBC();
        ZMusic.dataFolder = getDataFolder();
        if (!ZMusic.dataFolder.exists())
            ZMusic.dataFolder.mkdir();
        Config.debug = true;
        ZMusic.thisVer = getDescription().getVersion();
        ZMusic.log.sendNormalMessage("正在加载中....");
        MetricsBC bStats = new MetricsBC(this, 8864);
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
