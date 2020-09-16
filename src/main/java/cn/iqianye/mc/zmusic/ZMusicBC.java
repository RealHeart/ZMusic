package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.command.CmdBC;
import cn.iqianye.mc.zmusic.config.Conf;
import cn.iqianye.mc.zmusic.config.load.LoadBC;
import cn.iqianye.mc.zmusic.mod.SendBC;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.utils.log.LogBC;
import cn.iqianye.mc.zmusic.utils.message.MessageBC;
import cn.iqianye.mc.zmusic.utils.music.MusicBC;
import cn.iqianye.mc.zmusic.utils.other.OtherUtils;
import cn.iqianye.mc.zmusic.utils.player.PlayerBC;
import cn.iqianye.mc.zmusic.utils.runnable.RunTaskBC;
import cn.iqianye.mc.zmusic.utils.server.ServerBC;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.nio.file.Files;

public class ZMusicBC extends Plugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
        plugin = this;
        ZMusic.log = new LogBC(getProxy().getConsole());
        ZMusic.runTask = new RunTaskBC();
        ZMusic.message = new MessageBC();
        ZMusic.music = new MusicBC();
        ZMusic.send = new SendBC();
        ZMusic.player = new PlayerBC();
        ZMusic.server = new ServerBC();
        ZMusic.dataFolder = getDataFolder();
        Conf.debug = true;
        ZMusic.log.sendNormalMessage("正在加载中....");
        Conf.realSupportAdvancement = false;
        ZMusic.log.sendErrorMessage("BC端暂不支持进度提示!");
        Conf.realSupportBossBar = false;
        ZMusic.log.sendErrorMessage("BC端暂不支持BossBar!");
        Conf.realSupportTitle = false;
        ZMusic.log.sendErrorMessage("BC端暂不支持Title!");
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
        ZMusic.log.sendNormalMessage("正在注册Mod通信频道...");
        getProxy().getPluginManager().registerCommand(this, new CmdBC());
        OtherUtils.loginNetease();
        ZMusic.log.sendNormalMessage("-- §r[§eAllMusic§r]§a 频道注册完毕.");
        ZMusic.log.sendNormalMessage("成功加载配置文件!");
        ZMusic.log.sendNormalMessage("插件作者: 真心");
        ZMusic.log.sendNormalMessage("博客：www.zhenxin.xyz");
        ZMusic.log.sendNormalMessage("QQ：1307993674");
        ZMusic.log.sendNormalMessage("插件交流群：1032722724");
        ZMusic.log.sendNormalMessage("插件已加载完成!");
    }
}
