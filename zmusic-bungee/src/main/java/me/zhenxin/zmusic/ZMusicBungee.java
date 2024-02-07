package me.zhenxin.zmusic;

import me.zhenxin.zmusic.dependencies.annotation.RuntimeDependency;
import me.zhenxin.zmusic.platform.Platform;
import me.zhenxin.zmusic.platform.impl.LoggerBungee;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

/**
 * BungeeCord 入口类
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2023/8/28 12:26
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@RuntimeDependency(
        value = "!org.bstats:bstats-bungeecord:" + ZMusicConstants.BSTATS_VERSION,
        test = "!me.zhenxin.zmusic.library.bstats.bungeecord.Metrics",
        relocate = {"!org.bstats.", "!me.zhenxin.zmusic.library.bstats."}
)
public class ZMusicBungee extends Plugin {

    @Override
    public void onLoad() {
        ZMusicRuntime.setup(getDataFolder().getAbsolutePath(), getLogger(), ZMusicBungee.class);
    }

    @Override
    public void onEnable() {
        ZMusicKt.setLogger(new LoggerBungee(getProxy().getConsole()));
        ZMusicKt.setDataFolder(getDataFolder());
        ZMusicKt.setPlatform(Platform.BUNGEE);
        new Metrics(this, 8864);
        ZMusic.onEnable();
    }

    @Override
    public void onDisable() {
        ZMusic.onDisable();
    }
}
