package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.command.CommandExec;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.placeholderAPI.PlaceholderAPIHook;
import cn.iqianye.mc.zmusic.utils.LogUtils;
import cn.iqianye.mc.zmusic.utils.MessageUtils;
import cn.iqianye.mc.zmusic.utils.MusicUtils;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
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
        //注册命令对应的自动补全器
        getCommand("zm").setTabCompleter(new CommandExec());
        getServer().getPluginManager().registerEvents(this, this);
        OtherUtils.checkUpdate(Val.thisVer, null);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            LogUtils.sendNormalMessage("已检测到§ePlaceholderAPI§a, 正在注册...");
            boolean success = new PlaceholderAPIHook().register();
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
