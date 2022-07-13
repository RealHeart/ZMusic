package me.zhenxin.zmusic.bridge;

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
        plugin = this;
        ZMusicBridge.isSupportPlaceholderAPI = getServer()
                .getPluginManager()
                .isPluginEnabled("PlaceholderAPI");

    }
}
