package me.zhenxin.zmusic;

import me.zhenxin.zmusic.platform.impl.LoggerBukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ZMusic Bukkit 主入口
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2022/7/14 11:35
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class ZMusicBukkit extends JavaPlugin {

    @Override
    public void onLoad() {
        ZMusicRuntime.setup(getDataFolder().getAbsolutePath(), getLogger());
    }

    @Override
    public void onEnable() {
        ZMusicKt.setLogger(new LoggerBukkit(getServer().getConsoleSender()));
        ZMusicKt.setDataFolder(getDataFolder());
        ZMusic.INSTANCE.onEnable();
    }

    @Override
    public void onDisable() {
        ZMusic.INSTANCE.onDisable();
    }
}
