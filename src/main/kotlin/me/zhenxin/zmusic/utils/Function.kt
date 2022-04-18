package me.zhenxin.zmusic.utils

import com.alibaba.fastjson2.JSON
import me.zhenxin.adventure.text.Component
import me.zhenxin.adventure.text.minimessage.MiniMessage
import me.zhenxin.zmusic.api.MusicApi
import me.zhenxin.zmusic.api.impl.BiliBiliApi
import me.zhenxin.zmusic.api.impl.NeteaseApi
import me.zhenxin.zmusic.api.impl.SoundCloudApi
import me.zhenxin.zmusic.api.impl.XimaApi
import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.logger
import java.util.*


/**
 * 扩展函数
 * @author 真心
 * @since 2021/1/23 16:44
 * @email qgzhenxin@qq.com
 */

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
fun String.component(): Component = MiniMessage.miniMessage().deserialize(this)

/**
 * 通过代号获取相应API实例
 */
fun String.asMusicApi(): MusicApi {
    return when (this) {
        "netease" -> NeteaseApi() // 网易云音乐
        "bilibili" -> BiliBiliApi() // 哔哩哔哩
        "xima" -> XimaApi() // 喜马拉雅
        "soundcloud" -> SoundCloudApi() // SoundCloud
        else -> NeteaseApi() // 理论上永远不会执行
    }
}

/**
 * 设置语言
 */
fun setLocale() {
    try {
        val lang = config.LANGUAGE.split("_")
        Locale.setDefault(Locale(lang[0], lang[1]))
    } catch (e: Exception) {
        if (config.DEBUG) e.printStackTrace()
    }
}

/**
 * 检测服务器IP是否为中国大陆地区
 */
fun isChina(): Boolean {
    val result = httpGet("http://ip-api.com/json/")
    logger.debug(result)
    val data = JSON.parseObject(result.data)
    return data.getString("country") == "China"
}

/**
 * 格式化歌词
 * @param lyric String 歌词内容
 * @param translation String 歌词翻译内容 可空 默认为空
 * @return MutableList<LyricRaw>
 */
fun formatLyric(lyric: String, translation: String = ""): MutableList<LyricRaw> {
    val result = mutableListOf<LyricRaw>()
    val lyricMap = formatLyric(lyric)
    val translationMap = formatLyric(translation)
    lyricMap.forEach {
        val time = it.key
        val text = it.value
        val tr = translationMap[time] ?: ""
        result.add(LyricRaw(time, text, tr))
    }
    return result
}

private fun formatLyric(content: String): MutableMap<Long, String> {
    val map = mutableMapOf<Long, String>()
    val regex = Regex("\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})](.*)")
    val matches = regex.findAll(content)
    matches.forEach { value ->
        val min = value.groupValues[1].toLong()
        val sec = value.groupValues[2].toLong()
        val msStr = value.groupValues[3]
        val ms = if (msStr.length == 2) msStr.toLong() else msStr.substring(0, msStr.length - 1).toLong()
        val text = value.groupValues[4]
        val time = min * 60 * 1000 + sec * 1000 + ms
        map[time] = text.trim()
    }
    return map
}