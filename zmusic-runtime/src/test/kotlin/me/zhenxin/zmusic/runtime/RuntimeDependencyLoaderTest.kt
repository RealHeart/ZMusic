package me.zhenxin.zmusic.runtime

import kotlin.test.Test
import kotlin.test.assertTrue
import java.nio.file.Files

/**
 * [RuntimeDependencyLoader] 的集成测试。
 *
 * @author 真心
 * @since 5.0.0
 */
class RuntimeDependencyLoaderTest {
    /**
     * 验证依赖能下载到本地缓存，并可被隔离 classloader 加载。
     *
     * @throws Exception 临时目录、网络下载或 classloader 关闭失败时抛出
     */
    @Test
    fun `downloads dependency for isolated runtime classloader`() {
        val libraryDirectory = Files.createTempDirectory("zmusic-runtime-test").toFile()
        val logger = RecordingRuntimeLogger()
        val dependency = RuntimeDependency(
            group = "com.electronwill.night-config",
            artifact = "core",
            version = "3.9.0",
            testClass = "com.electronwill.nightconfig.core.Config"
        )

        val jars = RuntimeDependencyLoader(libraryDirectory, logger).resolve(listOf(dependency))
        val classLoader = IsolatedRuntimeClassLoader(
            jars.map { it.toURI().toURL() }.toTypedArray(),
            RuntimeDependencyLoader::class.java.classLoader
        )

        assertTrue(libraryDirectory.listFiles()?.any { it.extension == "jar" } == true)
        assertTrue(isClassAvailable(classLoader, dependency.testClass))
        assertTrue(logger.messages.any { it.contains("Runtime dependency resolved") })
        classLoader.close()
    }

    /**
     * 判断指定类是否能被 classloader 加载。
     *
     * @param classLoader 待验证的 classloader
     * @param className 类名
     * @return true 表示类可见
     */
    private fun isClassAvailable(classLoader: ClassLoader, className: String): Boolean {
        return runCatching {
            Class.forName(className, false, classLoader)
        }.isSuccess
    }

    /**
     * 测试用 logger。
     *
     * @author 真心
     * @since 5.0.0
     */
    private class RecordingRuntimeLogger : RuntimeLogger {
        /** 记录到的日志消息。 */
        val messages = mutableListOf<String>()

        /**
         * 记录 info 消息。
         *
         * @param message 日志内容
         */
        override fun info(message: String) {
            messages += message
        }

        /**
         * 记录 warn 消息。
         *
         * @param message 日志内容
         */
        override fun warn(message: String) {
            messages += message
        }

        /**
         * 记录 error 消息。
         *
         * @param message 日志内容
         * @param throwable 关联异常，可以为 null
         */
        override fun error(message: String, throwable: Throwable?) {
            messages += message
            throwable?.let { messages += it.stackTraceToString() }
        }
    }
}
