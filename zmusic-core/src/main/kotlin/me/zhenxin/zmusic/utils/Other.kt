package me.zhenxin.zmusic.utils

/**
 * 其他工具
 *
 * @author 真心
 * @since 2023/7/23 15:49
 * @email qgzhenxin@qq.com
 */

/**
 * 对字符串进行颜色处理
 */
fun String.colored() = this.replace("&", "§")

/**
 * 对字符串取消颜色处理
 */
fun String.uncolored() = this.replace("§", "&")