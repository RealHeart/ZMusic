package me.zhenxin.zmusic.bridge;

import me.zhenxin.zmusic.bridge.api.PlaceholderAPI;
import me.zhenxin.zmusic.bridge.api.PluginMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

/**
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2022/7/13 20:59
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class ZMusicBridge extends JavaPlugin {
    public static boolean isSupportPlaceholderAPI = false;
    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        if (getServer()
                .getPluginManager()
                .isPluginEnabled("ZMusic")) {
            Locale locale = Locale.getDefault();
            if (locale == Locale.SIMPLIFIED_CHINESE
                    || locale == Locale.TRADITIONAL_CHINESE
                    || locale == Locale.CHINESE) {
                getLogger().warning("检测到ZMusic插件已启用，请移除ZMusic插件。");
                getLogger().warning("ZMusicBridge 用于BungeeCord/Velocity端与子服通信，请勿同时启用ZMusic和ZMusicBridge。");
            } else {
                getLogger().warning("Checking ZMusic plugin is enabled, please remove ZMusic plugin.");
                getLogger().warning("ZMusicBridge is used to communicate between BungeeCord/Velocity server and sub-server, please do not enable ZMusic and ZMusicBridge at the same time.");
            }
        }
        plugin = this;
        ZMusicBridge.isSupportPlaceholderAPI = getServer()
                .getPluginManager()
                .isPluginEnabled("PlaceholderAPI");
        if (isSupportPlaceholderAPI) {
            boolean register = new PlaceholderAPI().register();
            if (!register) {
                getLogger().warning("PlaceholderAPI register failed!");
            }
        } else {
            getLogger().warning("PlaceholderAPI is not enabled.");
        }
        getServer()
                .getMessenger()
                .registerIncomingPluginChannel(this, "zmusic:bridge", new PluginMessage());
    }
}
