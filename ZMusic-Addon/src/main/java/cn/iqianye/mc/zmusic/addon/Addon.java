package cn.iqianye.mc.zmusic.addon;

import cn.iqianye.mc.zmusic.addon.api.PApiHook;
import cn.iqianye.mc.zmusic.addon.api.Version;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Addon extends JavaPlugin {
    public static JavaPlugin plugin;
    public static boolean isPapi;
    public static boolean isSupportAdv = true;

    @Override
    public void onEnable() {
        plugin = this;
        Version version = new Version();
        if (!getServer().getPluginManager().isPluginEnabled("ZMusic")) {
            if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                boolean b = new PApiHook().register();
                if (b) {
                    isPapi = true;
                    getLogger().info(ChatColor.GREEN + "PlaceholderAPI 注册成功");
                } else {
                    isPapi = false;
                    getLogger().warning(ChatColor.RED + "PlaceholderAPI 注册失败");
                }
            } else {
                isPapi = false;
                getLogger().warning(ChatColor.RED + "未找到PlaceholderAPI,PAPI转发不生效");
            }
            getServer().getMessenger().registerIncomingPluginChannel(this, "zmusic:channel", new PluginMessage());
            if (version.isLowerThan("1.12")) {
                getLogger().warning(ChatColor.RED + "版本低于1.12,进度提示不生效");
                isSupportAdv = false;
            }
        } else {
            getLogger().warning(ChatColor.RED + "请勿在子服安装ZMusic插件");
            setEnabled(false);
        }
    }
}