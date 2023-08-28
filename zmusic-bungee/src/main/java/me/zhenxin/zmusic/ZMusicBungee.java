package me.zhenxin.zmusic;

import me.zhenxin.zmusic.platform.impl.LoggerBungee;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * BungeeCord 入口类
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2023/8/28 12:26
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class ZMusicBungee extends Plugin {

    @Override
    public void onLoad() {
        ZMusicRuntime.setup(getDataFolder().getAbsolutePath(), getLogger());
    }

    @Override
    public void onEnable() {
        ZMusicKt.setLogger(new LoggerBungee(getProxy().getConsole()));
        ZMusicKt.setDataFolder(getDataFolder());
        ZMusic.INSTANCE.onEnable();
    }

    @Override
    public void onDisable() {
        ZMusic.INSTANCE.onDisable();
    }
}
