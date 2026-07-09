package me.zhenxin.zmusic.runtime

import me.zhenxin.zmusic.platform.PlatformContext
import java.io.File
import java.net.URL

/**
 * ZMusic core 的运行时启动器。
 *
 * @author 真心
 * @since 5.0.0
 */
object ZMusicRuntime {
    /**
     * 解析依赖、创建隔离 classloader，并启动 core 应用。
     *
     * @param platformName 平台显示名称
     * @param dataFolder 插件数据目录
     * @param logger runtime 日志出口
     * @param platformContext 平台上下文
     * @param applicationClassName core 应用类名
     * @return runtime 句柄
     * @throws Throwable core 启动失败时抛出原始异常
     */
    fun start(
        platformName: String,
        dataFolder: File,
        logger: RuntimeLogger,
        platformContext: PlatformContext,
        applicationClassName: String = "me.zhenxin.zmusic.ZMusicApplication"
    ): RuntimeHandle {
        ensureDataFolder(dataFolder, logger)
        logger.info("Initializing ZMusic runtime for $platformName.")
        val dependencyJars = RuntimeDependencyLoader(dataFolder.resolve("libraries"), logger).resolve(REQUIRED_DEPENDENCIES)
        val runtimeClassLoader = createRuntimeClassLoader(dependencyJars)
        verifyDependencies(runtimeClassLoader)

        val lifecycle = createLifecycle(runtimeClassLoader, applicationClassName, platformContext)
        return try {
            lifecycle.start()
            logger.info("ZMusic runtime initialized.")
            LifecycleHandle(lifecycle, logger, runtimeClassLoader)
        } catch (throwable: Throwable) {
            runtimeClassLoader.close()
            logger.error("Failed to initialize ZMusic runtime.", throwable)
            throw throwable
        }
    }

    /**
     * 创建 core 使用的 classloader。
     *
     * @param dependencyJars 动态依赖 jar 列表
     * @return 隔离 classloader
     */
    private fun createRuntimeClassLoader(dependencyJars: List<File>): IsolatedRuntimeClassLoader {
        val pluginUrl = ZMusicRuntime::class.java.protectionDomain.codeSource.location
        val urls = (dependencyJars.map { it.toURI().toURL() } + pluginUrl).toTypedArray<URL>()
        return IsolatedRuntimeClassLoader(urls, ZMusicRuntime::class.java.classLoader)
    }

    /**
     * 验证动态依赖是否可见。
     *
     * @param classLoader 用于加载依赖的 classloader
     * @throws ClassNotFoundException 依赖验证类缺失时抛出
     */
    private fun verifyDependencies(classLoader: ClassLoader) {
        REQUIRED_DEPENDENCIES.forEach { dependency ->
            Class.forName(dependency.testClass, false, classLoader)
        }
    }

    /**
     * 创建 core 生命周期对象。
     *
     * @param classLoader core classloader
     * @param applicationClassName core 应用类名
     * @param platformContext 平台上下文
     * @return core 生命周期对象
     * @throws ReflectiveOperationException 反射创建失败时抛出
     */
    private fun createLifecycle(
        classLoader: ClassLoader,
        applicationClassName: String,
        platformContext: PlatformContext
    ): RuntimeLifecycle {
        val applicationClass = Class.forName(applicationClassName, true, classLoader)
        val constructor = applicationClass.getConstructor(PlatformContext::class.java)
        return constructor.newInstance(platformContext) as RuntimeLifecycle
    }

    /**
     * 创建插件数据目录。
     *
     * @param dataFolder 插件数据目录
     * @param logger 日志出口
     */
    private fun ensureDataFolder(dataFolder: File, logger: RuntimeLogger) {
        if (dataFolder.exists()) {
            return
        }
        if (!dataFolder.mkdirs() && !dataFolder.exists()) {
            logger.warn("Failed to create data folder: ${dataFolder.absolutePath}")
        }
    }

    /**
     * core 生命周期句柄。
     *
     * @property lifecycle core 生命周期对象
     * @property logger 日志出口
     * @property runtimeClassLoader core classloader
     * @author 真心
     * @since 5.0.0
     */
    private class LifecycleHandle(
        private val lifecycle: RuntimeLifecycle,
        private val logger: RuntimeLogger,
        private val runtimeClassLoader: IsolatedRuntimeClassLoader
    ) : RuntimeHandle {
        /**
         * 停止 core 并关闭 classloader。
         */
        override fun stop() {
            runCatching {
                lifecycle.stop()
            }.onFailure { throwable ->
                logger.error("Failed to stop ZMusic runtime cleanly.", throwable)
            }
            runCatching {
                runtimeClassLoader.close()
            }.onFailure { throwable ->
                logger.error("Failed to close ZMusic runtime classloader.", throwable)
            }
        }
    }

    /** core 当前需要动态解析的依赖。 */
    private val REQUIRED_DEPENDENCIES = listOf(
        RuntimeDependency(
            group = "com.electronwill.night-config",
            artifact = "core",
            version = "3.9.0",
            testClass = "com.electronwill.nightconfig.core.Config"
        ),
        RuntimeDependency(
            group = "com.electronwill.night-config",
            artifact = "toml",
            version = "3.9.0",
            testClass = "com.electronwill.nightconfig.toml.TomlFormat"
        )
    )
}
