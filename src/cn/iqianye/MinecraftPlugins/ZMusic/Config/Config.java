package cn.iqianye.MinecraftPlugins.ZMusic.Config;

import cn.iqianye.MinecraftPlugins.ZMusic.Main;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.LogUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Config {
    // Prefix
    public static String prefix = ChatColor.AQUA + "ZMusic " + ChatColor.YELLOW + ">>> " + ChatColor.RESET;
    // Version
    public static int version;
    // LatestVersion
    public static int latestVersion = 2;
    // Debug
    public static boolean debug;
    // Account
    public static String neteasePhone;
    public static String neteasePassword;
    // Music
    public static int money;
    public static int cooldown;
    // Lyric
    public static boolean lyricEnable;
    public static boolean supportBossBar = false;
    public static boolean supportActionBar = false;
    public static boolean supportTitle = false;
    public static boolean supportChat = false;

    // RealSupport
    public static boolean realSupportBossBar = true;
    public static boolean realSupportActionBar = true;
    public static boolean realSupportTitle = true;
    public static boolean realSupportVault = true;

    public static void load(FileConfiguration configuration) {
        // Version
        version = configuration.getInt("version");
        if (version != latestVersion) {
            LogUtils.sendNormalMessage("-- 正在更新配置文件...");
            File config = new File(JavaPlugin.getPlugin(Main.class).getDataFolder() + File.separator + "config.yml");
            LogUtils.sendNormalMessage("-- 正在删除原配置文件...");
            config.delete();
            LogUtils.sendNormalMessage("-- 正在释放新配置文件...");
            JavaPlugin.getPlugin(Main.class).saveDefaultConfig();
            LogUtils.sendNormalMessage("-- 更新完毕.");
            JavaPlugin.getPlugin(Main.class).reloadConfig();
        }
        // Debug
        debug = configuration.getBoolean("debug");
        // Account
        neteasePhone = configuration.getString("account.netease.phone");
        neteasePassword = configuration.getString("account.netease.password");
        // Music
        money = configuration.getInt("music.money");
        cooldown = configuration.getInt("music.cooldown");
        // Lyric
        lyricEnable = configuration.getBoolean("lyric.enable");
        if (realSupportBossBar) {
            supportBossBar = configuration.getBoolean("lyric.bossBar");
        }
        if (realSupportActionBar) {
            supportActionBar = configuration.getBoolean("lyric.actionBar");
        }
        if (realSupportTitle) {
            supportTitle = configuration.getBoolean("lyric.subTitle");
        }
        supportChat = configuration.getBoolean("lyric.chatMessage");
    }
}