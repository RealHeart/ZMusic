package me.zhenxin.zmusic.entity

/**
 * 平台信息
 *
 * @author 真心
 * @since 2022/6/15 11:48
 */
data class PlatformInfo(
    val name: String,
    val display: String,
    val content: String
)

val platforms = mutableListOf<PlatformInfo>()