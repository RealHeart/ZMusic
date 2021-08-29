package me.zhenxin.zmusic.utils

import cn.hutool.crypto.SecureUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage


/**
 * 扩展函数
 * @author 真心
 * @since 2021/1/23 16:44
 * @email qgzhenxin@qq.com
 */

/**
 * 生成md5
 */
fun String.md5(): String = SecureUtil.md5(this)

/**
 * 替换&为§
 */
fun String.colored(): String = replace("&", "§")


/**
 * 替换&为§
 */
fun List<String>.colored(): List<String> = map { it.replace("&", "§") }

/**
 * 通过 MiniMessage 生成 Component
 */
fun String.component(): Component = MiniMessage.get().parse(this)