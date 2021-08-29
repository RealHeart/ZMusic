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
@RuntimeDependencies(
    RuntimeDependency(
        value = "!com.alibaba:fastjson:1.2.76",
        test = "!com.alibaba.fastjson.JSON"
    ),
    RuntimeDependency(
        value = "!cn.hutool:hutool-http:5.7.9",
        test = "!cn.hutool.http.HttpUtil"
    ),
    RuntimeDependency(
        value = "!cn.hutool:hutool-crypto:5.7.9",
        test = "!cn.hutool.crypto.SecureUtil"
    ),
    RuntimeDependency(
        value = "!me.zhenxin:adventure-text-minimessage:4.1.0-SNAPSHOT",
        test = "!net.kyori.adventure.text.minimessage.MiniMessage",
        repository = "https://gitee.com/RealHeart/Maven/raw/master"
    ),
)
class RuntimeEnv