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
        value = "!me.zhenxin:adventure-text-minimessage:$minimessage",
        test = "!me.zhenxin.adventure.text.minimessage.MiniMessage",
        repository = "https://gitee.com/RealHeart/Maven/raw/master"
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

private const val minimessage = "4.11.0"
private const val okhttp = "4.10.0"
private const val fastjson = "2.0.8"
private const val netty = "4.1.77.Final"
private const val nashorn = "15.4"
private const val nashornSandbox = "0.2.5"

