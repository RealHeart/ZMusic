package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.runtime.RuntimeHandle
import me.zhenxin.zmusic.runtime.ZMusicRuntime
import net.md_5.bungee.api.plugin.Plugin

/**
 * BungeeCord 平台插件入口。
 *
 * @author 真心
 * @since 5.0.0
 */
class BungeePlugin : Plugin() {
    private var runtimeHandle: RuntimeHandle? = null

    /**
     * 创建平台上下文并启动 core。
     */
    override fun onEnable() {
        val context = BungeePlatformContext(this)
        runtimeHandle = ZMusicRuntime.start(context.platform.displayName, dataFolder, context.logger, context)
    }

    /**
     * 停止 core。
     */
    override fun onDisable() {
        runtimeHandle?.stop()
        runtimeHandle = null
    }
}
