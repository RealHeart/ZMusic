package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.bstats.MetricsBC;
import cn.iqianye.mc.zmusic.command.CmdBC;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.config.LoadConfig;
import cn.iqianye.mc.zmusic.data.PlayerData;
import cn.iqianye.mc.zmusic.music.PlayListPlayer;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import cn.iqianye.mc.zmusic.utils.log.LogBC;
import cn.iqianye.mc.zmusic.utils.message.MessageBC;
import cn.iqianye.mc.zmusic.utils.mod.SendBC;
import cn.iqianye.mc.zmusic.utils.music.MusicBC;
import cn.iqianye.mc.zmusic.utils.player.PlayerBC;
import cn.iqianye.mc.zmusic.utils.runtask.RunTaskBC;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

public class ZMusicBC extends Plugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
        plugin = this;
        ZMusic.isBC = true;
        ZMusic.log = new LogBC(getProxy().getConsole());
        ZMusic.runTask = new RunTaskBC();
        ZMusic.message = new MessageBC();
        ZMusic.music = new MusicBC();
        ZMusic.send = new SendBC();
        ZMusic.player = new PlayerBC();
        ZMusic.dataFolder = getDataFolder();
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        Config.debug = true;
        MetricsBC bStats = new MetricsBC(this, 8864);
        getProxy().registerChannel("zmusic:channel");
        getProxy().registerChannel("allmusic:channel");
        getProxy().registerChannel("AudioBuffer");
        ZMusic.log.sendNormalMessage("正在加载中....");
        new LoadConfig().load();
        ZMusic.thisVer = getDescription().getVersion();
        getProxy().getPluginManager().registerCommand(this, new CmdBC());
        OtherUtils.loginNetease();
        ZMusic.log.sendNormalMessage("成功加载配置文件!");
        ZMusic.log.sendNormalMessage("插件作者: 真心");
        ZMusic.log.sendNormalMessage("博客：www.zhenxin.xyz");
        ZMusic.log.sendNormalMessage("QQ：1307993674");
        ZMusic.log.sendNormalMessage("插件交流群：1032722724");
        ZMusic.log.sendNormalMessage("插件已加载完成!");
    }

    @Override
    public void onDisable() {
        ZMusic.log.sendNormalMessage("正在卸载中....");
        List<ProxiedPlayer> players = new ArrayList<>(getProxy().getPlayers());
        if (!players.isEmpty()) {
            for (ProxiedPlayer player : players) {
                OtherUtils.resetPlayerStatus(player);
                PlayListPlayer plp = PlayerData.getPlayerPlayListPlayer(player);
                if (plp != null) {
                    plp.isStop = true;
                    PlayerData.setPlayerPlayListPlayer(player, null);
                    OtherUtils.resetPlayerStatus(player);
                }
            }
        }
        ZMusic.log.sendNormalMessage("插件作者: 真心");
        ZMusic.log.sendNormalMessage("博客：www.zhenxin.xyz");
        ZMusic.log.sendNormalMessage("QQ：1307993674");
        ZMusic.log.sendNormalMessage("插件交流群：1032722724");
        ZMusic.log.sendNormalMessage("插件已卸载完成!");
    }
}
