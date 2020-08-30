package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.api.AdvancementAPI;
import cn.iqianye.mc.zmusic.api.Version;
import cn.iqianye.mc.zmusic.command.CommandExec;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.music.PlayList;
import cn.iqianye.mc.zmusic.music.PlayListPlayer;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.pApi.PApiHook;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.*;
import cn.iqianye.mc.zmusic.bStats.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        LogUtils.sendNormalMessage("正在加载中....");
        //注册bStats
        MetricsLite metricsLite = new MetricsLite(this, 7291);
        //注册命令对应的执行器
        getCommand("zm").setExecutor(new CommandExec());
        getCommand("zmusic").setExecutor(new CommandExec());
        //注册命令对应的自动补全器
        getCommand("zm").setTabCompleter(new CommandExec());
        getCommand("zmusic").setTabCompleter(new CommandExec());
        getServer().getPluginManager().registerEvents(this, this);
        OtherUtils.checkUpdate(Val.thisVer, null);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
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
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            LogUtils.sendNormalMessage("已检测到Vault, 经济功能生效.");
        } else {
            LogUtils.sendErrorMessage("未找到Vault, 经济相关功能不生效.");
            Config.realSupportVault = false;
        }
        Version version = new Version();
        if (version.isLowerThan("1.7.10")) {
            LogUtils.sendErrorMessage("检测到当前服务端版本低于1.7.10，不支持Title显示");
            Config.realSupportTitle = false;
        }
        if (version.isLowerThan("1.9")) {
            LogUtils.sendErrorMessage("检测到当前服务端版本低于1.9，不支持BossBar");
            LogUtils.sendErrorMessage("检测到当前服务端版本低于1.9，不支持ActionBar");
            Config.realSupportBossBar = false;
            Config.realSupportActionBar = false;
        }
        if (version.isLowerThan("1.12")) {
            LogUtils.sendErrorMessage("检测到当前服务端版本低于1.12，不支持进度提示");
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
        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        if (!players.isEmpty()) {
            OtherUtils.resetPlayerStatusAll(players);
            MusicUtils.stopAll(players);
        }
        for (Player player : players) {
            PlayListPlayer plp = PlayerStatus.getPlayerPlayListPlayer(player);
            if (plp != null) {
                plp.isStop = true;
                PlayerStatus.setPlayerPlayListPlayer(player, null);
                OtherUtils.resetPlayerStatus(player);
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

}
