package me.zhenxin.zmusic.utils

import com.alibaba.fastjson2.JSON
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
    val api = config.API_NETEASE_LINK
    val result = httpPost(
        "$api/login", mutableMapOf(
            "email" to account,
            "md5_password" to password.digest("md5")
        )
    )

    val info = JSON.parseObject(result.data)
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