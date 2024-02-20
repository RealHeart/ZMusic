package me.zhenxin.zmusic.entity

/**
 * 版本信息
 *
 * @author 真心
 * @since 2024/2/20 10:27
 */
class VersionInfo(
    /**
     * 版本号
     */
    val version: String,
    /**
     * 版本代码
     */
    val versionCode: Long,
    /**
     * 更新日志
     */
    val changelog: String,
    /**
     * 下载链接
     */
    val download: String,
)
