package cn.iqianye.MinecraftPlugins.ZMusic.Config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    // Prefix
    public static String prefix = ChatColor.AQUA + "ZMusic " + ChatColor.YELLOW + ">>> " + ChatColor.RESET;
    // Version
    public static int version;
    // Debug
    public static boolean debug;
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
        // Debug
        debug = configuration.getBoolean("debug");
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