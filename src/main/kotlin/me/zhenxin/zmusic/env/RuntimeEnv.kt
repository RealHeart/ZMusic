@file:Suppress("unused", "SpellCheckingInspection")

package me.zhenxin.zmusic.env

import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency

@RuntimeDependencies(
    RuntimeDependency(
        value = "!cn.hutool:hutool-http:$hutool",
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

private const val hutool = "5.8.4"
private const val netty = "4.1.77.Final"
private const val nashorn = "15.4"
private const val nashornSandbox = "0.2.5"

