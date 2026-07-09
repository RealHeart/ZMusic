package me.zhenxin.zmusic.runtime

import java.net.URL
import java.net.URLClassLoader

/**
 * 隔离加载 core 和运行时依赖。
 *
 * @author 真心
 * @since 5.0.0
 */
class IsolatedRuntimeClassLoader(
    urls: Array<URL>,
    parent: ClassLoader
) : URLClassLoader(urls, parent) {
    /**
     * 加载指定类。
     *
     * @param name 类名
     * @param resolve 是否解析类
     * @return 已加载的类
     * @throws ClassNotFoundException 找不到类时抛出
     */
    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        if (isParentFirst(name)) {
            return super.loadClass(name, resolve)
        }

        synchronized(getClassLoadingLock(name)) {
            findLoadedClass(name)?.let { return it }

            val loaded = runCatching { findClass(name) }
                .getOrElse { super.loadClass(name, false) }

            if (resolve) {
                resolveClass(loaded)
            }
            return loaded
        }
    }

    /**
     * 判断是否应优先委托父加载器。
     *
     * @param name 类名
     * @return true 表示使用父加载器优先
     */
    private fun isParentFirst(name: String): Boolean {
        return name.startsWith("java.") ||
            name.startsWith("javax.") ||
            name.startsWith("kotlin.") ||
            name.startsWith("org.jetbrains.") ||
            name.startsWith("me.zhenxin.zmusic.runtime.") ||
            name.startsWith("me.zhenxin.zmusic.platform.") ||
            name.startsWith("org.bukkit.") ||
            name.startsWith("net.md_5.") ||
            name.startsWith("com.velocitypowered.")
    }
}
