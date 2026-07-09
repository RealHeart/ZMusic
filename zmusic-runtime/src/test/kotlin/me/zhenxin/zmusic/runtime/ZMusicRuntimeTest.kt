package me.zhenxin.zmusic.runtime

import me.zhenxin.zmusic.platform.CommandRegistry
import me.zhenxin.zmusic.platform.Platform
import me.zhenxin.zmusic.platform.PlatformCommand
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.PlatformLogger
import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.Scheduler
import me.zhenxin.zmusic.platform.entity.ZPlayer
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * [ZMusicRuntime] 的启动流程测试。
 *
 * @author 真心
 * @since 5.0.0
 */
class ZMusicRuntimeTest {
    /**
     * 验证 runtime 准备依赖后会启动指定生命周期类。
     *
     * @throws Throwable runtime 启动失败时抛出
     */
    @Test
    fun `starts lifecycle after preparing runtime dependencies`() {
        val dataFolder = Files.createTempDirectory("zmusic-runtime-start-test").toFile()
        val logger = RecordingRuntimeLogger()
        val context = TestPlatformContext(dataFolder, logger)

        val handle = ZMusicRuntime.start(
            platformName = "Test",
            dataFolder = dataFolder,
            logger = logger,
            platformContext = context,
            applicationClassName = TestRuntimeApplication::class.java.name
        )

        assertTrue(TestRuntimeApplication.started)
        assertTrue(dataFolder.resolve("libraries").isDirectory)

        handle.stop()

        assertTrue(TestRuntimeApplication.stopped)
    }

    /**
     * 测试用 core 应用。
     *
     * @property context 平台上下文
     * @author 真心
     * @since 5.0.0
     */
    class TestRuntimeApplication(private val context: PlatformContext) : RuntimeLifecycle {
        /**
         * 标记应用已启动。
         */
        override fun start() {
            started = true
            context.logger.info("test application started")
        }

        /**
         * 标记应用已停止。
         */
        override fun stop() {
            stopped = true
        }

        /**
         * 测试状态。
         *
         * @author 真心
         * @since 5.0.0
         */
        companion object {
            /** 是否已启动。 */
            var started = false
            /** 是否已停止。 */
            var stopped = false
        }
    }

    /**
     * 测试用平台上下文。
     *
     * @property dataFolder 数据目录
     * @property logger 日志适配器
     * @author 真心
     * @since 5.0.0
     */
    private class TestPlatformContext(
        override val dataFolder: java.io.File,
        override val logger: PlatformLogger
    ) : PlatformContext {
        /** 测试平台类型。 */
        override val platform: Platform = Platform.BUKKIT
        /** 空命令注册器。 */
        override val commandRegistry: CommandRegistry = CommandRegistry { }
        /** 空玩家网关。 */
        override val players: PlayerGateway = object : PlayerGateway {
            override fun onlinePlayers(): Collection<ZPlayer> = emptyList()
            override fun findByName(name: String): ZPlayer? = null
        }
        /** 空插件消息网关。 */
        override val pluginMessenger: PluginMessenger = object : PluginMessenger {
            override fun registerChannel(channel: String) {}
            override fun unregisterChannel(channel: String) {}
            override fun send(player: ZPlayer, channel: String, payload: ByteArray) {}
        }
        /** 立即执行任务的调度器。 */
        override val scheduler: Scheduler = object : Scheduler {
            override fun async(task: () -> Unit) = task()
            override fun main(task: () -> Unit) = task()
        }
    }

    /**
     * 测试用 logger。
     *
     * @author 真心
     * @since 5.0.0
     */
    private class RecordingRuntimeLogger : PlatformLogger {
        /**
         * 忽略 info 消息。
         *
         * @param message 日志内容
         */
        override fun info(message: String) {
        }

        /**
         * 忽略 warn 消息。
         *
         * @param message 日志内容
         */
        override fun warn(message: String) {
        }

        /**
         * 忽略 error 消息。
         *
         * @param message 日志内容
         * @param throwable 关联异常，可以为 null
         */
        override fun error(message: String, throwable: Throwable?) {
        }
    }
}
