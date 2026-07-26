package me.zhenxin.zmusic

import me.zhenxin.zmusic.command.CommandManager
import me.zhenxin.zmusic.config.ConfigLoader
import me.zhenxin.zmusic.music.NoopMusicCatalog
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.PlayerListener
import me.zhenxin.zmusic.platform.entity.ZPlayer
import me.zhenxin.zmusic.playback.PlaybackService
import me.zhenxin.zmusic.playback.PlayerStateRegistry
import me.zhenxin.zmusic.realtime.PluginRealtimeClient
import me.zhenxin.zmusic.runtime.RuntimeLifecycle

/**
 * ZMusic core 应用入口。
 *
 * @property context 平台上下文
 * @property configLoader 配置加载器
 * @property musicCatalog 音乐搜索来源
 * @author 真心
 * @since 5.0.0
 */
class ZMusicApplication(
    private val context: PlatformContext,
    private val configLoader: ConfigLoader = ConfigLoader(context.dataFolder),
    private val musicCatalog: NoopMusicCatalog = NoopMusicCatalog()
) : RuntimeLifecycle {
    /**
     * runtime 反射使用的构造函数。
     *
     * @param context 平台上下文
     */
    constructor(context: PlatformContext) : this(
        context = context,
        configLoader = ConfigLoader(context.dataFolder),
        musicCatalog = NoopMusicCatalog()
    )

    private var started = false
    private var config = configLoader.load()
    private val playerStates = PlayerStateRegistry()
    private val playbackService = PlaybackService(context, playerStates)
    private var realtimeClient = PluginRealtimeClient(context, config.api, playerStates, playbackService)
    private val playerListener = object : PlayerListener {
        override fun onJoin(player: ZPlayer) {
            playerStates.state(player)
            realtimeClient.publishPlayerJoined(player)
        }

        override fun onQuit(player: ZPlayer) {
            playerStates.remove(player)
            realtimeClient.publishPlayerLeft(player)
        }
    }

    /**
     * 平台命令系统实际注册的命令处理器。
     */
    val commandManager = CommandManager(
        context = context,
        reload = ::reload,
        musicCatalog = musicCatalog,
        playbackService = playbackService,
        version = ZMusicInfo.VERSION
    )

    /**
     * 注册命令和插件消息通道。
     */
    override fun start() {
        if (started) {
            return
        }
        started = true
        context.logger.info("ZMusic ${ZMusicInfo.VERSION} starting on ${context.platform.displayName}")
        context.commandRegistry.register(commandManager)
        context.players.registerListener(playerListener)
        playbackService.start(config.channel, realtimeClient::publishClientEvent)
        realtimeClient.start()
        context.logger.info("ZMusic started.")
    }

    /**
     * 注销插件消息通道。
     */
    override fun stop() {
        if (!started) {
            return
        }
        realtimeClient.stop()
        playbackService.stop()
        context.players.unregisterListener()
        playerStates.clear()
        started = false
        context.logger.info("ZMusic stopped.")
    }

    /**
     * 重载配置，并在通道名变化时重新注册通道。
     *
     * @return true 表示重载成功
     */
    fun reload(): Boolean {
        return runCatching {
            val nextConfig = configLoader.load()
            playbackService.changeChannel(nextConfig.channel)
            if (nextConfig.api != config.api) {
                realtimeClient.stop()
                realtimeClient = PluginRealtimeClient(context, nextConfig.api, playerStates, playbackService)
                playbackService.stop()
                playbackService.start(nextConfig.channel, realtimeClient::publishClientEvent)
                realtimeClient.start()
            }
            config = nextConfig
        }.onFailure { throwable ->
            context.logger.error("Failed to reload ZMusic.", throwable)
        }.isSuccess
    }
}
