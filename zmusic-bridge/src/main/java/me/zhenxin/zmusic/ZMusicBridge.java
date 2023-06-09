package me.zhenxin.zmusic;

import me.zhenxin.zmusic.api.PlaceholderAPI;
import me.zhenxin.zmusic.api.PluginMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.logging.Logger;

/**
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2022/7/13 20:59
 */
@SuppressWarnings({"AlibabaClassNamingShouldBeCamel", "AlibabaUndefineMagicConstant"})
public class ZMusicBridge extends JavaPlugin {
    public static boolean DEBUG = false;
    public static Logger logger;
    public static boolean isSupportPlaceholderAPI = false;
    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        if (getServer()
                .getPluginManager()
                .isPluginEnabled("ZMusic")) {
            Locale locale = Locale.getDefault();
            if ("zh".equals(locale.getLanguage())) {
                getLogger().warning("检测到ZMusic插件已启用，请移除ZMusic插件。");
                getLogger().warning("ZMusicBridge 用于BungeeCord/Velocity端与子服通信，请勿同时启用ZMusic和ZMusicBridge。");
            } else {
                getLogger().warning("Checking ZMusic plugin is enabled, please remove ZMusic plugin.");
                getLogger().warning("ZMusicBridge is used to communicate between BungeeCord/Velocity server and sub-server, please do not enable ZMusic and ZMusicBridge at the same time.");
            }
            setEnabled(false);
        }
        plugin = this;
        logger = getLogger();
        ZMusicBridge.isSupportPlaceholderAPI = getServer()
                .getPluginManager()
                .isPluginEnabled("PlaceholderAPI");
        if (isSupportPlaceholderAPI) {
            boolean register = new PlaceholderAPI().register();
            if (!register) {
                getLogger().warning("PlaceholderAPI register failed!");
            } else {
                getLogger().info("PlaceholderAPI register success!");
            }
        } else {
            getLogger().warning("PlaceholderAPI is not enabled.");
        }
        String channel = "zmusic:bridge";
        getServer().getMessenger().registerIncomingPluginChannel(this, channel, new PluginMessage());
        getLogger().info("ZMusicBridge enabled!");
    }
}
