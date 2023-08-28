package me.zhenxin.zmusic;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.zhenxin.zmusic.platform.impl.LoggerVelocity;

import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Velocity 入口类
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2023/8/28 12:22
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Plugin(id = "zmusic")
public class ZMusicVelocity {
    private final ProxyServer server;
    private final Path dataDirectory;

    @Inject
    public ZMusicVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.dataDirectory = dataDirectory;

        ZMusicRuntime.setup(dataDirectory.toFile().getAbsolutePath(), logger);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        ZMusicKt.setLogger(new LoggerVelocity(server.getConsoleCommandSource()));
        ZMusicKt.setDataFolder(dataDirectory.toFile());
        ZMusic.INSTANCE.onEnable();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        ZMusic.INSTANCE.onDisable();
    }
}
