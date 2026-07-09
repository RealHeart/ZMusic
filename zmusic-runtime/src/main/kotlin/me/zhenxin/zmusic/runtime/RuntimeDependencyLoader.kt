package me.zhenxin.zmusic.runtime

import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.security.MessageDigest

/**
 * 负责下载和缓存 runtime 依赖 jar。
 *
 * @property libraryDirectory 本地依赖缓存目录
 * @property logger 下载过程使用的日志出口
 * @author 真心
 * @since 5.0.0
 */
class RuntimeDependencyLoader(
    private val libraryDirectory: File,
    private val logger: RuntimeLogger
) {
    /**
     * 返回所有依赖对应的本地 jar 文件，缺失时自动下载。
     *
     * @param dependencies 需要解析的依赖列表
     * @return 本地 jar 文件列表
     * @throws IllegalStateException 下载失败时抛出
     */
    fun resolve(dependencies: List<RuntimeDependency>): List<File> {
        if (!libraryDirectory.exists()) {
            libraryDirectory.mkdirs()
        }

        return dependencies.map { dependency ->
            val jarFile = libraryDirectory.resolve(stableFileName(dependency))
            if (!jarFile.exists()) {
                download(dependency, jarFile)
            }
            logger.info("Runtime dependency resolved: ${dependency.coordinates}")
            jarFile
        }
    }

    /**
     * 下载到临时文件后再移动到目标文件。
     *
     * @param dependency 需要下载的依赖
     * @param target 目标 jar 文件
     * @throws IllegalStateException 下载失败时抛出
     */
    private fun download(dependency: RuntimeDependency, target: File) {
        val url = dependency.remoteUrl()
        logger.info("Downloading runtime dependency: ${dependency.coordinates}")

        val tempFile = File(target.parentFile, "${target.name}.tmp")
        val connection = URI(url).toURL().openConnection() as HttpURLConnection
        connection.connectTimeout = 15_000
        connection.readTimeout = 30_000
        connection.instanceFollowRedirects = true
        connection.requestMethod = "GET"

        try {
            val code = connection.responseCode
            if (code !in 200..299) {
                error("Failed to download ${dependency.coordinates}: HTTP $code from $url")
            }
            connection.inputStream.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            if (!tempFile.renameTo(target)) {
                tempFile.copyTo(target, overwrite = true)
                tempFile.delete()
            }
        } finally {
            connection.disconnect()
        }
    }

    /**
     * 生成缓存文件名。
     *
     * @param dependency 依赖信息
     * @return 带短哈希的 jar 文件名
     */
    private fun stableFileName(dependency: RuntimeDependency): String {
        val hash = sha1(dependency.coordinates).take(10)
        return "${dependency.artifact}-${dependency.version}-$hash.jar"
    }

    /**
     * 计算 SHA-1 字符串。
     *
     * @param value 原始文本
     * @return 十六进制 SHA-1
     */
    private fun sha1(value: String): String {
        val digest = MessageDigest.getInstance("SHA-1").digest(value.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }
}
