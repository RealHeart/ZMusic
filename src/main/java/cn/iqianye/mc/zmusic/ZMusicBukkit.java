package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.api.Version;
import cn.iqianye.mc.zmusic.command.CmdBukkit;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.music.PlayListPlayer;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.pApi.PApiHook;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.stats.bStats;
import cn.iqianye.mc.zmusic.stats.cStats;
import cn.iqianye.mc.zmusic.utils.LogUtils;
import cn.iqianye.mc.zmusic.utils.MessageUtils;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

public class ZMusicBukkit extends JavaPlugin implements Listener {

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
        plugin = this;
        Config.debug = true;
        Val.thisVer = getDescription().getVersion();
        Version version = new Version();
        LogUtils.sendNormalMessage("正在加载中....");
        //注册bStats
        bStats bStats = new bStats(this, 7291);
        //注册cStats
        cStats cStats = new cStats(this);
        //注册命令对应的执行器
        getCommand("zm").setExecutor(new CmdBukkit());
        //注册命令对应的自动补全器
        getCommand("zm").setTabCompleter(new CmdBukkit());
        if (getServer().getPluginManager().isPluginEnabled("AudioBuffer")) {
            LogUtils.sendErrorMessage("请勿安装AudioBuffer插件.");
            Val.isEnable = false;
        }
        if (getServer().getPluginManager().isPluginEnabled("AllMusic")) {
            LogUtils.sendErrorMessage("请勿安装AllMusic插件.");
            Val.isEnable = false;
        }
        //注册Mod通信频道
        LogUtils.sendNormalMessage("正在注册Mod通信频道...");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "allmusic:channel");
        LogUtils.sendNormalMessage("-- §r[§eAllMusic§r]§a 频道注册完毕.");
        if (!version.isHigherThan("1.12")) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, "AudioBuffer");
            LogUtils.sendNormalMessage("-- §r[§eAudioBuffer§r]§a 频道注册完毕.");
        } else {
            LogUtils.sendErrorMessage("-- §r[§eAudioBuffer§r]§c 服务端大于1.12，频道注册取消.");
        }
        //注册事件监听器
        getServer().getPluginManager().registerEvents(this, this);
        OtherUtils.checkUpdate(Val.thisVer, null);
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            LogUtils.sendNormalMessage("已检测到§ePlaceholderAPI§a, 正在注册...");
            boolean success = new PApiHook().register();
            if (success) {
                LogUtils.sendNormalMessage("-- §r[§ePlaceholderAPI§r] §a注册成功!");
            } else {
                LogUtils.sendErrorMessage("-- §r[§ePlaceholderAPI§r] §c注册失败!");
            }
        } else {
            LogUtils.sendErrorMessage("未找到§ePlaceholderAPI§c, §ePlaceholderAPI§c相关功能不生效.");
        }
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            LogUtils.sendNormalMessage("已检测到Vault, 经济功能生效.");
        } else {
            LogUtils.sendErrorMessage("未找到Vault, 经济相关功能不生效.");
            Config.realSupportVault = false;
        }
        if (getServer().getPluginManager().isPluginEnabled("ViaVersion")) {
            LogUtils.sendNormalMessage("已检测到ViaVersion, 高版本转发功能生效.");
        } else {
            LogUtils.sendErrorMessage("未找到ViaVersion, 高版本转发功能不生效.");
            Val.isViaVer = false;
        }
        if (version.isLowerThan("1.8")) {
            if (version.isEquals("1.7.10")) {
                if (!Bukkit.getName().contains("Uranium")) {
                    LogUtils.sendErrorMessage("检测到当前服务端非Uranium，不支持Title/ActionBar显示");
                    Config.realSupportTitle = false;
                    Config.realSupportActionBar = false;
                }
            } else {
                LogUtils.sendErrorMessage("检测到当前服务端版本低于1.8，不支持Title/ActionBar显示");
                Config.realSupportTitle = false;
                Config.realSupportActionBar = false;
            }
        }
        if (version.isLowerThan("1.9")) {
            LogUtils.sendErrorMessage("检测到当前服务端版本低于1.9，不支持BossBar");
            Config.realSupportBossBar = false;
        }
        if (version.isLowerThan("1.12")) {
            LogUtils.sendErrorMessage("检测到当前服务端版本低于1.12，不支持Hud显示");
            Config.realSupportHud = false;
            LogUtils.sendErrorMessage("检测到当前服务端版本低于1.12，不支持进度");
            Config.realSupportAdvancement = false;
        }
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            saveDefaultConfig();
            LogUtils.sendErrorMessage("无法找到配置文件,正在创建!");
        }
        reloadConfig();
        Config.load(getConfig());
        OtherUtils.loginNetease(null);
        LogUtils.sendNormalMessage("成功加载配置文件!");
        LogUtils.sendNormalMessage("插件作者: 真心");
        LogUtils.sendNormalMessage("博客：www.zhenxin.xyz");
        LogUtils.sendNormalMessage("QQ：1307993674");
        LogUtils.sendNormalMessage("插件交流群：1032722724");
        LogUtils.sendNormalMessage("插件已加载完成!");
    }

    @Override
    public void onDisable() {
        LogUtils.sendNormalMessage("正在卸载中....");
        List<Player> players = new ArrayList<>(getServer().getOnlinePlayers());
        if (!players.isEmpty()) {
            for (Player player : players) {
                OtherUtils.resetPlayerStatus(player);
                PlayListPlayer plp = PlayerStatus.getPlayerPlayListPlayer(player);
                if (plp != null) {
                    plp.cancel();
                    PlayerStatus.setPlayerPlayListPlayer(player, null);
                    OtherUtils.resetPlayerStatus(player);
                }
            }
        }
        LogUtils.sendNormalMessage("插件作者: 真心");
        LogUtils.sendNormalMessage("博客：www.zhenxin.xyz");
        LogUtils.sendNormalMessage("QQ：1307993674");
        LogUtils.sendNormalMessage("插件交流群：1032722724");
        LogUtils.sendNormalMessage("插件已卸载完成!");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                if (player.hasPermission("zmusic.admin") || player.isOp()) {
                    OtherUtils.checkUpdate(Val.thisVer, player);
                    if (!Val.isLatest) {
                        MessageUtils.sendNormalMessage("发现新版本 V" + Val.latestVer, player);
                        MessageUtils.sendNormalMessage("更新日志:", player);
                        String[] updateLog = Val.updateLog.split("\\n");
                        for (String s : updateLog) {
                            MessageUtils.sendNormalMessage(s, player);
                        }
                        MessageUtils.sendNormalMessage("下载地址: " + ChatColor.YELLOW + ChatColor.UNDERLINE + Val.downloadUrl, player);
                    }
                }
            }
        }.runTaskLaterAsynchronously(this, 40L);
    }

}
