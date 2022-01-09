package me.zhenxin.zmusic.utils

import com.alibaba.fastjson.JSONObject
import me.zhenxin.adventure.text.Component
import me.zhenxin.adventure.text.minimessage.MiniMessage
import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.impl.*
import me.zhenxin.zmusic.module.config
import taboolib.common.io.digest
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
fun String.component(): Component = MiniMessage.get().parse(this)

/**
 * 通过代号获取相应API实例
 */
fun String.asMusicApi(): MusicApi {
    return when (this) {
        "netease" -> NeteaseApi() // 网易云音乐
        "qq" -> QQMusicApi() // QQ音乐
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
    val data = JSONObject.parseObject(result.data as String)
    return data.getString("country") == "China"
}

/**
 * 登录网易云音乐
 */
fun loginNetease(): LoginResult {
    val account = config.API_NETEASE_ACCOUNT
    val password = config.API_NETEASE_PASSWORD
    if (account.isEmpty() || password.isEmpty()) {
        return LoginResult(400, "&c未配置账号密码")
    }
    val api = config.API_NETEASE_LINK
    val result = httpPost(
        "$api/login", mutableMapOf(
            "email" to account,
            "md5_password" to password.digest("md5")
        )
    )

    val info = JSONObject.parseObject(result.data as String)
    val code = info.getIntValue("code")
    return if (code != 200) {
        val msg = info.getString("msg")
        LoginResult(code, "&c$msg")
    } else {
        val profile = info.getJSONObject("profile")
        val nickname = profile.getString("nickname")
        LoginResult(200, "&a登录成功, 欢迎您 &b$nickname")
    }
}

/**
 * 登录QQ音乐
 */
fun loginQQ() {

}