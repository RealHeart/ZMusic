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
        value = "!com.alibaba:fastjson:$fastjson",
        test = "!com.alibaba.fastjson.JSON"
    ),
    RuntimeDependency(
        value = "!me.zhenxin:adventure-text-minimessage:$minimessage",
        test = "!me.zhenxin.adventure.text.minimessage.MiniMessage",
        repository = "https://gitee.com/RealHeart/Maven/raw/master"
    ),
    RuntimeDependency(
        value = "!io.netty:netty-buffer:4.1.72.Final",
        test = "!io.netty.buffer.ByteBuf"
    )
)
class RuntimeEnv

private const val minimessage = "4.2.0-SNAPSHOT"
private const val okhttp = "4.9.3"
private const val fastjson = "1.2.79"