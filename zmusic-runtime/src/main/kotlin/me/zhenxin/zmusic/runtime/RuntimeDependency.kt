package me.zhenxin.zmusic.runtime

/**
 * 运行时需要解析的 Maven 依赖。
 *
 * @property group Maven groupId
 * @property artifact Maven artifactId
 * @property version Maven 版本
 * @property testClass 下载后用于验证 classloader 可见性的类名
 * @property repository Maven 仓库地址
 * @author 真心
 * @since 5.0.0
 */
data class RuntimeDependency(
    val group: String,
    val artifact: String,
    val version: String,
    val testClass: String,
    val repository: String = "https://repo1.maven.org/maven2"
) {
    /**
     * 标准 Maven 坐标。
     */
    val coordinates: String get() = "$group:$artifact:$version"

    /**
     * Maven 仓库中的 jar 文件名。
     */
    val fileName: String get() = "$artifact-$version.jar"

    /**
     * 计算该依赖在 Maven 仓库中的 jar URL。
     *
     * @return 远程 jar URL
     */
    fun remoteUrl(): String {
        val groupPath = group.replace('.', '/')
        return "${repository.trimEnd('/')}/$groupPath/$artifact/$version/$fileName"
    }
}
