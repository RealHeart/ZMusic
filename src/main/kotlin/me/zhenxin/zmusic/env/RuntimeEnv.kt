@file:Suppress("unused", "SpellCheckingInspection")

package me.zhenxin.zmusic.env

import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency

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
    ),
    RuntimeDependency(
        value = "!org.openjdk.nashorn:nashorn-core:$nashorn",
        test = "!jdk.nashorn.api.scripting.NashornScriptEngineFactory"
    ),
    RuntimeDependency(
        value = "!org.javadelight:delight-nashorn-sandbox:$nashornSandbox",
        test = "!delight.nashornsandbox.NashornSandbox"
    )
)
class RuntimeEnv

private const val adventureMinimessage = "4.11.0"
private const val adventurePlatform = "4.1.0"
private const val okhttp = "4.9.3"
private const val fastjson = "2.0.6"
private const val netty = "4.1.77.Final"
private const val nashorn = "15.4"
private const val nashornSandbox = "0.2.5"

