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
        value = "!com.squareup.okhttp3:okhttp:4.9.1",
        test = "!okhttp3.OkHttp"
    )
)
class RuntimeEnv