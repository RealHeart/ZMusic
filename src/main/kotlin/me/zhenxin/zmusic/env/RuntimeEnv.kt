@file:Suppress("SpellCheckingInspection")

package me.zhenxin.zmusic.env

import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency

/**
 * 运行环境依赖声明
 *
 * @author 真心
 * @since 2021/8/17 14:48
 * @email qgzhenxin@qq.com
 */
@Suppress("unused", "SpellCheckingInspection")
@RuntimeDependencies(
    RuntimeDependency(
        value = "!com.squareup.okhttp3:okhttp:$okhttp",
        test = "!okhttp3.OkHttpClient",
        relocate = ["!kotlin.", "!kotlin@kotlin_version_escape@."]
    ),
    RuntimeDependency(
        value = "!com.alibaba.fastjson2:fastjson2:$fastjson",
        test = "!com.alibaba.fastjson2.JSON"
    ),
    RuntimeDependency(
        value = "!net.kyori:adventure-text-minimessage:$adventureMinimessage",
        test = "!net.kyori.adventure.text.minimessage.MiniMessage",
    ),
    RuntimeDependency(
        value = "!net.kyori:adventure-platform-bukkit:$adventurePlatform",
        test = "!net.kyori.adventure.platform.bukkit.BukkitAudiences",
    ),
    RuntimeDependency(
        value = "!net.kyori:adventure-platform-bungeecord:$adventurePlatform",
        test = "!net.kyori.adventure.platform.bungeecord.BungeeAudiences",
    ),
    RuntimeDependency(
        value = "!io.netty:netty-buffer:$netty",
        test = "!io.netty.buffer.ByteBuf"
    )
)
class RuntimeEnv

private const val adventureMinimessage = "4.11.0"
private const val adventurePlatform = "4.1.0"
private const val okhttp = "4.9.3"
private const val fastjson = "2.0.3"
private const val netty = "4.1.77.Final"