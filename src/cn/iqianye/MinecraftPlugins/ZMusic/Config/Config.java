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
    public static boolean supportBossBar;
    public static boolean supportActionBar;
    public static boolean supportTitle;
    public static boolean supportChat;

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
        supportBossBar = configuration.getBoolean("lyric.bossBar");
        supportActionBar = configuration.getBoolean("lyric.actionBar");
        supportTitle = configuration.getBoolean("lyric.subTitle");
        supportChat = configuration.getBoolean("lyric.chatMessage");
    }
}