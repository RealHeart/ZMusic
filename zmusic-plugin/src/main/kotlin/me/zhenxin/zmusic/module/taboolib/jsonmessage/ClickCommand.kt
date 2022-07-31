package me.zhenxin.zmusic.module.taboolib.jsonmessage

/**
 * 点击命令
 *
 * @author 真心
 * @since 2022/7/3 14:53
 */
data class ClickCommand(
    val message: String,
    val hover: String,
    val command: String
)
