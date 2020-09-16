package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.bstats.MetricsBC;
import cn.iqianye.mc.zmusic.command.CmdBC;
import cn.iqianye.mc.zmusic.config.Conf;
import cn.iqianye.mc.zmusic.config.load.LoadBC;
import cn.iqianye.mc.zmusic.mod.SendBC;
import cn.iqianye.mc.zmusic.music.PlayListPlayer;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import cn.iqianye.mc.zmusic.utils.log.LogBC;
import cn.iqianye.mc.zmusic.utils.message.MessageBC;
import cn.iqianye.mc.zmusic.utils.music.MusicBC;
import cn.iqianye.mc.zmusic.utils.player.PlayerBC;
import cn.iqianye.mc.zmusic.utils.runnable.RunTaskBC;
import cn.iqianye.mc.zmusic.utils.server.ServerBC;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ZMusicBC extends Plugin {
    public static Plugin plugin;

    public static void sendCustomData(ProxiedPlayer player, String data1, int data2) {
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
        // perform a check to see if globally are no players
        if (networkPlayers == null || networkPlayers.isEmpty()) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("papi"); // the channel could be whatever you want
        out.writeUTF(data1); // this data could be whatever you want
        out.writeInt(data2); // this data could be whatever you want

        // we send the data to the server
        // using ServerInfo the packet is being queued if there are no players in the server
        // using only the server to send data the packet will be lost if no players are in it
        player.getServer().getInfo().sendData("zmusic:channel", out.toByteArray());
    }

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
        ZMusic.server = new ServerBC();
        ZMusic.dataFolder = getDataFolder();
        Conf.debug = true;
        MetricsBC bStats = new MetricsBC(this, 8864);
        getProxy().registerChannel("zmusic:channel");
        ZMusic.log.sendNormalMessage("正在加载中....");
        Conf.realSupportAdvancement = false;
        ZMusic.log.sendErrorMessage("BC端暂不支持进度提示!");
        Conf.realSupportVault = false;
        ZMusic.log.sendErrorMessage("BC端暂不支持经济系统!");
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        Val.thisVer = getDescription().getVersion();
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            LoadBC.load(ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                PlayListPlayer plp = PlayerStatus.getPlayerPlayListPlayer(player);
                if (plp != null) {
                    plp.isStop = true;
                    PlayerStatus.setPlayerPlayListPlayer(player, null);
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
