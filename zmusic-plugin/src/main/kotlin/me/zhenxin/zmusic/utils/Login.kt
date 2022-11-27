package me.zhenxin.zmusic.utils

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.config.config
import taboolib.common.io.digest

/**
 * 登录
 *
 * @author 真心
 * @since 2022/1/27 17:22
 */

/**
 * 登录网易云音乐
 */
fun loginNetease(): LoginResult {
    val account = config.API_NETEASE_ACCOUNT
    val password = config.API_NETEASE_PASSWORD
    if (account.isEmpty() || password.isEmpty()) {
        return LoginResult(400, "&c未配置账号密码")
    }
    val api = config.API_NETEASE
    val result = post(
        "$api/login", mutableMapOf(
            "email" to account,
            "md5_password" to password.digest("md5")
        )
    )

    val info = JSONObject(result)
    val code = info.getInt("code")
    return if (code != 200) {
        val msg = info.getStr("msg")
        LoginResult(code, "&c$msg")
    } else {
        val profile = info.getJSONObject("profile")
        val nickname = profile.getStr("nickname")
        LoginResult(200, "&a登录成功, 欢迎您 &b$nickname")
    }
}