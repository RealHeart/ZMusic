package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.addon.PApiHook;
import cn.iqianye.mc.zmusic.api.Version;
import cn.iqianye.mc.zmusic.bstats.MetricsBukkit;
import cn.iqianye.mc.zmusic.command.CmdBukkit;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.event.EventBukkit;
import cn.iqianye.mc.zmusic.utils.CookieUtils;
import cn.iqianye.mc.zmusic.utils.log.LogBukkit;
import cn.iqianye.mc.zmusic.utils.message.MessageBukkit;
import cn.iqianye.mc.zmusic.utils.mod.SendBukkit;
import cn.iqianye.mc.zmusic.utils.music.Music;
import cn.iqianye.mc.zmusic.utils.player.PlayerBukkit;
import cn.iqianye.mc.zmusic.utils.runtask.RunTaskBukkit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ZMusicBukkit extends JavaPlugin {

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        ZMusic.log = new LogBukkit(getServer().getConsoleSender());
        ZMusic.log.sendNormalMessage("正在加载中....");
        plugin = this;
        ZMusic.isBC = false;
        ZMusic.runTask = new RunTaskBukkit();
        ZMusic.message = new MessageBukkit();
        ZMusic.music = new Music();
        ZMusic.send = new SendBukkit();
        ZMusic.player = new PlayerBukkit();
        ZMusic.dataFolder = getDataFolder();
        if (!ZMusic.dataFolder.exists()) {
            ZMusic.dataFolder.mkdir();
        }
        Config.debug = true;
        ZMusic.thisVer = getDescription().getVersion();
        Version version = new Version();
        CookieUtils.initCookieManager();
        //注册bStats
        new MetricsBukkit(this, 7291);
        //注册命令对应的执行器
        getCommand("zm").setExecutor(new CmdBukkit());
        //注册命令对应的自动补全器
        getCommand("zm").setTabCompleter(new CmdBukkit());
        if (getServer().getPluginManager().isPluginEnabled("AudioBuffer")) {
            ZMusic.log.sendErrorMessage("请勿安装AudioBuffer插件.");
            ZMusic.isEnable = false;
        }
        if (getServer().getPluginManager().isPluginEnabled("AllMusic")) {
            ZMusic.log.sendErrorMessage("请勿安装AllMusic插件.");
            ZMusic.isEnable = false;
        }
        //注册Mod通信频道
        ZMusic.log.sendNormalMessage("正在注册Mod通信频道...");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "allmusic:channel");
        ZMusic.log.sendNormalMessage("-- §r[§eAllMusic§r]§a 频道注册完毕.");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "zmusic:channel");
        ZMusic.log.sendNormalMessage("-- §r[§eZMusic Mod§r]§a 频道注册完毕.");
        if (!version.isHigherThan("1.12")) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, "AudioBuffer");
            ZMusic.log.sendNormalMessage("-- §r[§eAudioBuffer§r]§a 频道注册完毕.");
        } else {
            ZMusic.log.sendErrorMessage("-- §r[§eAudioBuffer§r]§c 服务端大于1.12，频道注册取消.");
        }
        //注册事件监听器
        getServer().getPluginManager().registerEvents(new EventBukkit(), this);
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            ZMusic.log.sendNormalMessage("已检测到§ePlaceholderAPI§a, 正在注册...");
            boolean success = new PApiHook().register();
            if (success) {
                ZMusic.log.sendNormalMessage("-- §r[§ePlaceholderAPI§r] §a注册成功!");
            } else {
                ZMusic.log.sendErrorMessage("-- §r[§ePlaceholderAPI§r] §c注册失败!");
            }
        } else {
            ZMusic.log.sendErrorMessage("未找到§ePlaceholderAPI§c, §ePlaceholderAPI§c相关功能不生效.");
        }
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            ZMusic.log.sendNormalMessage("已检测到Vault, 经济功能生效.");
        } else {
            ZMusic.log.sendErrorMessage("未找到Vault, 经济相关功能不生效.");
            Config.realSupportVault = false;
        }
        if (getServer().getPluginManager().isPluginEnabled("ViaVersion")) {
            ZMusic.log.sendNormalMessage("已检测到ViaVersion, 高版本转发功能生效.");
        } else {
            ZMusic.log.sendErrorMessage("未找到ViaVersion, 高版本转发功能不生效.");
            ZMusic.isViaVer = false;
        }
        if (version.isLowerThan("1.8")) {
            if (version.isEquals("1.7.10")) {
                if (!Bukkit.getName().contains("Uranium")) {
                    ZMusic.log.sendErrorMessage("检测到当前服务端非Uranium，不支持Title/ActionBar显示");
                    Config.realSupportTitle = false;
                    Config.realSupportActionBar = false;
                }
            } else {
                ZMusic.log.sendErrorMessage("检测到当前服务端版本低于1.8，不支持Title/ActionBar显示");
                Config.realSupportTitle = false;
                Config.realSupportActionBar = false;
            }
        }
        if (version.isLowerThan("1.9")) {
            ZMusic.log.sendErrorMessage("检测到当前服务端版本低于1.9，不支持BossBar");
            Config.realSupportBossBar = false;
        }
        if (version.isLowerThan("1.12")) {
            ZMusic.log.sendErrorMessage("检测到当前服务端版本低于1.12，不支持Hud显示");
            Config.realSupportHud = false;
            ZMusic.log.sendErrorMessage("检测到当前服务端版本低于1.12，不支持进度");
            Config.realSupportAdvancement = false;
        }

        ZMusic.loadEnd(getServer().getConsoleSender());
    }

    @Override
    public void onDisable() {
        ZMusic.disable();
    }

}
