package me.zhenxin.zmusic.entity

/**
 * 版本信息
 *
 * @author 真心
 * @since 2024/2/20 10:27
 */
data class VersionInfo(
    /**
     * 版本号
     */
    var version: String = "",
    /**
     * 版本代码
     */
    var versionCode: Long = 0,
    /**
     * 更新日志
     */
    var changelog: String = "",
    /**
     * 下载链接
     */
    var download: String = ""
)
