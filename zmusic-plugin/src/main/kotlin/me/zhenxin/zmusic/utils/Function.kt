@file:Suppress("SpellCheckingInspection")

package me.zhenxin.zmusic.utils

import cn.hutool.json.JSONArray
import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.data.ZMusicData
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.module.music.MusicApi
import me.zhenxin.zmusic.module.music.impl.*
import me.zhenxin.zmusic.module.taboolib.sendMsg
import taboolib.common.platform.ProxyCommandSender
import java.util.*


/**
 * 扩展函数
 * @author 真心
 * @since 2021/1/23 16:44
 * @email qgzhenxin@qq.com
 */

/**
 * 格式化颜色代码
 */
fun String.colored() = replace("&", "§")

/**
 * 通过代号获取相应API实例
 */
fun String.asMusicApi(): MusicApi {
    return when (this) {
        "netease" -> NeteaseApi() // 网易云音乐
        "bilibili" -> BiliBiliApi() // 哔哩哔哩
        "xima" -> XimaApi() // 喜马拉雅
        "soundcloud" -> SoundCloudApi() // SoundCloud
        "youtube" -> YoutubeApi() // YouTube
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
    val data = queryIP()
    return data.getStr("country") == "China"
}

/**
 * 获取公网IP地址
 */
fun realIP(): String {
    val data = queryIP()
    return data.getStr("query")
}

/**
 * IP请求
 */
private fun queryIP(): JSONObject {
    val result = get("http://ip-api.com/json/")
    return JSONObject(result)
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

private fun formatLyric(content: String): MutableMap<Int, String> {
    val map = mutableMapOf<Int, String>()
    val regex = Regex("\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})](.*)")
    val matches = regex.findAll(content)
    matches.forEach { value ->
        val min = value.groupValues[1].toLong()
        val sec = value.groupValues[2].toLong()
        val ms = value.groupValues[3].toLong()
        val text = value.groupValues[4]
        val time = (min * 60 * 1000 + sec * 1000 + ms) / 1000
        map[time.toInt()] = text.trim()
    }
    return map
}

fun checkUpdate(sender: ProxyCommandSender) {
    sender.sendMsg(Lang.UPDATE_CHECKING)
    val plugin = "zmusic"
    val type = "snapshot"
    val api = "https://api.zplu.cc/version"
    val result = get("$api?plugin=$plugin&type=$type")
    val json = JSONObject(result)
    val data = json.getJSONObject("data")
    val info = data.getJSONObject("info")
    val version = info.getStr("version")
    val versionCode = info.getInt("version_code")
    val changelog = info.getStr("changelog")
    val releaseUrl = info.getStr("release_url")
    if (versionCode > ZMusicData.VERSION_CODE) {
        Lang.UPDATE_NEW_VERSION.forEach {
            sender.sendMsg(
                it
                    .replace("{0}", version)
                    .replace("{1}", releaseUrl)
            )
        }
        val logs = changelog.split("\\n")
        logs.forEach {
            sender.sendMsg("&b$it")
        }
    } else {
        sender.sendMsg(Lang.UPDATE_NO_UPDATE)
    }
}

fun Any.toJSONObject(): JSONObject = JSONObject(this)

fun Any.toJSONArray(): JSONArray = JSONArray(this)