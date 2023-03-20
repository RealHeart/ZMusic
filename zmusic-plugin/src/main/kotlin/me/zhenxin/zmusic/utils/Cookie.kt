package me.zhenxin.zmusic.utils

import cn.hutool.http.cookie.GlobalCookieManager
import cn.hutool.json.JSONArray
import cn.hutool.json.JSONObject
import taboolib.common.platform.function.getDataFolder
import java.io.File
import java.net.HttpCookie
import java.net.URI

/**
 * Cookie 工具
 *
 * @author 真心
 * @since 2023/3/20 11:31
 * @email qgzhenxin@qq.com
 */


/**
 * 初始化 Cookie
 */
fun initCookie() {
    val cookieManager = GlobalCookieManager.getCookieManager()
    val file = File(getDataFolder(), "data/cookies.json")
    val str = file.readText()
    if (str.isNotEmpty()) {
        val cookies = JSONArray(str)
        cookies.forEach {
            it as JSONObject
            val name = it.getStr("name")
            val value = it.getStr("value")
            val domain = it.getStr("domain")
            val maxAge = it.getLong("maxAge")
            val path = it.getStr("path")
            val secure = it.getBool("secure")
            val httpOnly = it.getBool("httpOnly")
            val version = it.getInt("version")
            val cookie = HttpCookie(name, value)
            cookie.domain = domain
            cookie.maxAge = maxAge
            cookie.path = path
            cookie.secure = secure
            cookie.isHttpOnly = httpOnly
            cookie.version = version
            val uri = URI.create(domain)
            cookieManager.cookieStore.add(uri, cookie)
        }
    }
}

/**
 * 保存 Cookie
 */
fun saveCookie() {
    val cookieManager = GlobalCookieManager.getCookieManager()
    val cookies = cookieManager.cookieStore.cookies
    val file = File(getDataFolder(), "data/cookies.json")
    val json = JSONArray()
    cookies.forEach {
        json.add(JSONObject(it))
    }
    file.writeText(json.toString())
}