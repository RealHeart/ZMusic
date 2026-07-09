package me.zhenxin.zmusic.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import me.zhenxin.zmusic.ZMusicInfo
import me.zhenxin.zmusic.runtime.RuntimeHandle
import me.zhenxin.zmusic.runtime.ZMusicRuntime
import org.slf4j.Logger
import java.nio.file.Path

/**
 * Velocity 平台插件入口。
 *
 * @property server Velocity 代理服务
 * @property logger Velocity logger
 * @property dataDirectory 插件数据目录
 * @author 真心
 * @since 5.0.0
 */
@Plugin(
    id = "zmusic",
    name = "ZMusic",
    version = ZMusicInfo.VERSION,
    authors = [ZMusicInfo.AUTHOR, ZMusicInfo.ORGANIZATION]
)
class VelocityPlugin @Inject constructor(
    private val server: ProxyServer,
    private val logger: Logger,
    @DataDirectory private val dataDirectory: Path
) {
    private var runtimeHandle: RuntimeHandle? = null

    /**
     * 创建平台上下文并启动 core。
     *
     * @param event 代理初始化事件
     */
    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        val context = VelocityPlatformContext(server, logger, dataDirectory, this)
        runtimeHandle = ZMusicRuntime.start(context.platform.displayName, dataDirectory.toFile(), context.logger, context)
    }

    /**
     * 停止 core。
     *
     * @param event 代理关闭事件
     */
    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent) {
        runtimeHandle?.stop()
        runtimeHandle = null
    }
}
