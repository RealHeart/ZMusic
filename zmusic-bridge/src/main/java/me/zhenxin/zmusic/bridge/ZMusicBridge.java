package me.zhenxin.zmusic.bridge;

import me.zhenxin.zmusic.bridge.api.PlaceholderAPI;
import me.zhenxin.zmusic.bridge.api.PluginMessage;
import org.bukkit.plugin.java.JavaPlugin;

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
            setEnabled(false);
        }
        plugin = this;
        ZMusicBridge.isSupportPlaceholderAPI = getServer()
                .getPluginManager()
                .isPluginEnabled("PlaceholderAPI");
        if (isSupportPlaceholderAPI) {
            new PlaceholderAPI().register();
        }
        getServer()
                .getMessenger()
                .registerIncomingPluginChannel(this, "zmusic:bridge", new PluginMessage());
    }
}
