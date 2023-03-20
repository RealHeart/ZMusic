package me.zhenxin.zmusic.login

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.utils.post
import me.zhenxin.zmusic.utils.saveCookie
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.submit
import java.net.URLEncoder

/**
 * 网易云登录操作
 *
 * @author 真心
 * @since 2022/11/27 18:32
 * @email qgzhenxin@qq.com
 */
object NeteaseLogin {
    val api by lazy { Config.API_NETEASE_LINK }

    fun qrcode(sender: ProxyCommandSender) {
        val key = key()
        val qrcode = create(key)
        sender.sendMsg("请打开如下网址扫描二维码登录:")
        sender.sendMsg(
            "https://cli.im/api/qrcode/code?text=${
                URLEncoder.encode(qrcode, "UTF-8")
            }"
        )
        submit(async = true, period = 60) {
            val result = check(key)
            val data = JSONObject(result)
            val code = data.getInt("code")
            when (code) {
                800 -> {
                    sender.sendMsg("二维码已过期!")
                    cancel()
                }

                802 -> {
                    val nickname = data.getStr("nickname")
                    sender.sendMsg("$nickname, 请在手机上确认登录!")
                }

                803 -> {
                    welcome(sender)
                    saveCookie()
                    cancel()
                }
            }
        }
    }

    private fun create(key: String): String {
        val result = post("$api/login/qr/create", mapOf("key" to key), disableCache = true)
        val json = JSONObject(result)
        val data = json.getJSONObject("data")
        return data.getStr("qrurl")
    }

    private fun check(key: String): JSONObject {
        val result = post("$api/login/qr/check", mapOf("key" to key), disableCache = true)
        return JSONObject(result)
    }

    private fun key(): String {
        val result = post("$api/login/qr/key", disableCache = true)
        val json = JSONObject(result)
        val data = json.getJSONObject("data")
        return data.getStr("unikey")
    }

    fun refresh(sender: ProxyCommandSender) {
        var cookie = ""
        val result = post("$api/login/refresh", disableCache = true)
        val data = JSONObject(result)
        val code = data.getInt("code")
        if (code == 200) {
            cookie = data.getStr("cookie")
        }
        if (cookie.isNotEmpty()) {
            sender.sendMsg("刷新登录状态成功!")
            welcome(sender)
        } else {
            sender.sendMsg("刷新登录状态失败, 建议执行重新登录操作!")
        }
    }

    fun guest(sender: ProxyCommandSender) {
        post("$api/register/anonimous", disableCache = true)
        welcome(sender)
    }

    fun isLogin(): Boolean {
        val result = post("$api/login/status", disableCache = true)
        val data = JSONObject(result).getJSONObject("data")
        val profile = data.getJSONObject("profile")
        return !profile.isNullOrEmpty()
    }

    private fun welcome(sender: ProxyCommandSender) {
        val name = userNickname()
        if (name.isEmpty()) {
            sender.sendMsg("昵称获取失败!")
        } else {
            sender.sendMsg("登录成功! 欢迎您, $name!")
        }
    }

    private fun userNickname(): String {
        val result = post("$api/user/account", disableCache = true)
        val data = JSONObject(result)
        if (data.getInt("code") != 200) {
            return ""
        }
        val profile = data.getJSONObject("profile")
        if (profile.isNullOrEmpty()) {
            return data.getJSONObject("account").getStr("userName")
        }
        return profile.getStr("nickname")
    }
}