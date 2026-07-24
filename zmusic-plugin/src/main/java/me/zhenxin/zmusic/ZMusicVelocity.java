package me.zhenxin.zmusic;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import me.zhenxin.zmusic.command.CmdVelocity;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.event.EventVelocity;
import me.zhenxin.zmusic.utils.CookieUtils;
import me.zhenxin.zmusic.utils.log.LogVelocity;
import me.zhenxin.zmusic.utils.message.MessageVelocity;
import me.zhenxin.zmusic.utils.mod.SendVelocity;
import me.zhenxin.zmusic.utils.music.Music;
import me.zhenxin.zmusic.utils.player.PlayerVelocity;
import me.zhenxin.zmusic.utils.runtask.RunTaskVelocity;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "zmusic",
        name = "ZMusic",
        version = "2.13.5-beta",
        description = "A Minecraft music plugin supporting Bukkit, BungeeCord, and Velocity",
        url = "https://github.com/zhenxin/ZMusic",
        authors = {"ZhenXin"}
)
public class ZMusicVelocity {

    public static ProxyServer server;
    public static ZMusicVelocity plugin;
    public static Logger logger;
    public static Path dataDirectory;

    // 插件消息通道
    public static final MinecraftChannelIdentifier ZMUSIC_CHANNEL =
            MinecraftChannelIdentifier.create("zmusic", "channel");
    public static final MinecraftChannelIdentifier ALLMUSIC_CHANNEL =
            MinecraftChannelIdentifier.create("allmusic", "channel");

    @Inject
    public ZMusicVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        ZMusicVelocity.server = server;
        ZMusicVelocity.logger = logger;
        ZMusicVelocity.dataDirectory = dataDirectory;
        ZMusicVelocity.plugin = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        ZMusic.log = new LogVelocity();
        ZMusic.isBC = false;
        ZMusic.isVelocity = true;
        ZMusic.runTask = new RunTaskVelocity();
        ZMusic.message = new MessageVelocity();
        ZMusic.music = new Music();
        ZMusic.send = new SendVelocity();
        ZMusic.player = new PlayerVelocity();
        ZMusic.dataFolder = dataDirectory.toFile();
        if (!ZMusic.dataFolder.exists()) {
            ZMusic.dataFolder.mkdir();
        }
        Config.realSupportBossBar = false;
        Config.realSupportVault = false;
        Config.debug = true;
        ZMusic.thisVer = "2.13.5-beta";
        ZMusic.log.sendNormalMessage("正在加载中....");
        CookieUtils.initCookieManager();

        // 注册插件消息通道
        server.getChannelRegistrar().register(ZMUSIC_CHANNEL);
        server.getChannelRegistrar().register(ALLMUSIC_CHANNEL);

        // 注册命令
        CommandManager commandManager = server.getCommandManager();
        CommandMeta meta = commandManager.metaBuilder("zm")
                .aliases("zmusic", "music")
                .plugin(this)
                .build();
        commandManager.register(meta, new CmdVelocity());

        // 注册事件监听器
        server.getEventManager().register(this, new EventVelocity());

        ZMusic.loadEnd(null);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        ZMusic.disable();
    }
}
