package me.zhenxin.zmusic.config

import me.zhenxin.zmusic.utils.getStr
import taboolib.common5.util.decodeBase64
import taboolib.common5.util.encodeBase64
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import java.nio.charset.StandardCharsets

/**
 * 数据存储
 *
 * @author 真心
 * @since 2022/11/29 10:27
 * @email qgzhenxin@qq.com
 */
object GlobalData {
    @Config("data/global.yml")
    private lateinit var globalData: Configuration

    val MODE
        get() = globalData.getInt("mode")

    val PLAYLIST
        get() = globalData.getMapList("playlist")

    var COOKIE
        get() = globalData.getStr("cookie").decodeBase64().toString(StandardCharsets.UTF_8)
        set(value) {
            val v = value.encodeBase64()
            globalData["cookie"] = v
            globalData.saveToFile()
            reload()
        }

    fun reload() {
        globalData.reload()
    }
}