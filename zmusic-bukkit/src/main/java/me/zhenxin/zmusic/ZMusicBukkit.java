package me.zhenxin.zmusic;

import me.zhenxin.zmusic.dependencies.annotation.RuntimeDependency;
import me.zhenxin.zmusic.platform.Platform;
import me.zhenxin.zmusic.platform.impl.LoggerBukkit;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ZMusic Bukkit 主入口
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2022/7/14 11:35
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@RuntimeDependency(
        value = "!org.bstats:bstats-bukkit:" + ZMusicConstants.BSTATS_VERSION,
        test = "!me.zhenxin.zmusic.library.bstats.bukkit.Metrics",
        relocate = {"!org.bstats.", "!me.zhenxin.zmusic.library.bstats."}
)
public class ZMusicBukkit extends JavaPlugin {

    @Override
    public void onLoad() {
        ZMusicRuntime.setup(getDataFolder().getAbsolutePath(), getLogger(), ZMusicBukkit.class);
    }

    @Override
    public void onEnable() {
        ZMusicKt.setLogger(new LoggerBukkit(getServer().getConsoleSender()));
        ZMusicKt.setDataFolder(getDataFolder());
        ZMusicKt.setPlatform(Platform.BUKKIT);
        new Metrics(this, 7291);
        ZMusic.INSTANCE.onEnable();
    }

    @Override
    public void onDisable() {
        ZMusic.INSTANCE.onDisable();
    }
}
