package me.zhenxin.zmusic.utils

/**
 * 字符串工具
 *
 * @author 真心
 * @since 2024/2/20 10:22
 */

/**
 * 对字符串进行颜色处理
 */
fun String.colored() = this.replace("&", "§")

/**
 * 对字符串取消颜色处理
 */
fun String.uncolored() = this.replace("§", "&")
