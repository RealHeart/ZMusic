package me.zhenxin.zmusic.login

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.utils.post
import taboolib.common.platform.ProxyCommandSender

/**
 * 网易云登录操作
 *
 * @author 真心
 * @since 2022/11/27 18:32
 * @email qgzhenxin@qq.com
 */
object NeteaseLogin {
    val api by lazy { Config.API_NETEASE_LINK }

    /**
     * 登录
     * @param phone Long 手机号
     * @param captcha Int 验证码
     * @param sender ProxyCommandSender 发送者
     */
    fun login(phone: Long, captcha: Int, sender: ProxyCommandSender) {
        val cookie = loginCellphone(phone, captcha)
        if (cookie.isNotEmpty()) {
            welcome(sender)
        } else {
            sender.sendMsg("登录失败, 请检查参数是否正确!")
        }
    }

    /**
     * 发送验证码
     * @param phone Long 手机号
     * @param sender ProxyCommandSender 发送者
     */
    fun sent(phone: Long, sender: ProxyCommandSender) {
        val success = sentCaptcha(phone)
        if (success) {
            sender.sendMsg("已发送验证码!")
        } else {
            sender.sendMsg("发送验证码失败!")
        }
    }

    /**
     * 刷新登录状态
     * @param sender ProxyCommandSender 发送者
     */
    fun refresh(sender: ProxyCommandSender) {
        val cookie = loginRefresh()
        if (cookie.isNotEmpty()) {
            sender.sendMsg("刷新登录状态成功!")
            welcome(sender)
        } else {
            sender.sendMsg("刷新登录状态失败, 建议执行重新登录操作!")
        }
    }

    /**
     * 游客登录
     */
    fun loginGuest(sender: ProxyCommandSender) {
        val result = post("$api/register/anonimous")
        val data = JSONObject(result)
        val cookies = data.getStr("cookie")
        welcome(sender)
    }

    /**
     * 是否已登录
     */
    fun isLogin(): Boolean {
        return userNickname().isNotEmpty()
    }

    /**
     * 发送欢迎信息
     * @param sender ProxyCommandSender 发送者
     */
    private fun welcome(sender: ProxyCommandSender) {
        val name = userNickname()
        if (name.isEmpty()) {
            sender.sendMsg("昵称获取失败!")
        } else {
            sender.sendMsg("登录成功! 欢迎您, $name!")
        }
    }

    /**
     * 发送验证码
     * @param phone Long 手机号
     * @return Boolean 是否成功
     */
    private fun sentCaptcha(phone: Long): Boolean {
        val result = post(
            "$api/captcha/sent", mutableMapOf(
                "phone" to phone
            )
        )
        val data = JSONObject(result)
        return data.getInt("code") == 200
    }

    /**
     * 通过验证码登录
     * @param phone Long 手机号
     * @param captcha Int 验证码
     * @return Boolean 是否登录成功
     */
    private fun loginCellphone(phone: Long, captcha: Int): String {
        val result = post(
            "$api/login/cellphone", mutableMapOf(
                "phone" to phone,
                "captcha" to captcha
            )
        )
        val data = JSONObject(result)
        val code = data.getInt("code")
        if (code != 200) return ""
        return data.getStr("cookie")
    }

    /**
     * 刷新登录状态
     * @return Boolean 是否成功
     */
    private fun loginRefresh(): String {
        val result = post("$api/login/refresh")
        val data = JSONObject(result)
        val code = data.getInt("code")
        if (code != 200) return ""
        return data.getStr("cookie")
    }

    /**
     * 获取账号昵称
     * @return String 昵称
     */
    private fun userNickname(): String {
        val result = post("$api/user/account")
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