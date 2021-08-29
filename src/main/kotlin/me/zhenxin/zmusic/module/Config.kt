package me.zhenxin.zmusic.module

import me.zhenxin.zmusic.utils.colored
import taboolib.common.platform.function.disablePlugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile

/**
 * 配置文件
 *
 * @author 真心
 * @since 2021/8/14 20:02
 * @email qgzhenxin@qq.com
 */
object Config {
    /** 配置文件对象 */
    @Config(migrate = true)
    private lateinit var config: SecuredFile

    /** 配置文件版本 */
    var VERSION = 0

    /** 检查更新 */
    var CHECK_UPDATE = true

    /** 语言 */
    var LANGUAGE = ""

    /** 前缀 */
    var PREFIX = ""

    /** 调试模式 */
    var DEBUG = false

    /** 网易云音乐 API链接 */
    var API_NETEASE_LINK = ""

    /** 网易云音乐 账号 */
    var API_NETEASE_ACCOUNT = ""

    /** 网易云音乐 密码 */
    var API_NETEASE_PASSWORD = ""

    /** QQ音乐 API链接  */
    var API_QQ_LINK = ""

    /** QQ音乐 Cookie */
    var API_QQ_COOKIE = ""

    /** ZMusic VIP QQ */
    var VIP_QQ = ""

    /** ZMusic VIP Key */
    var VIP_KEY = ""

    /** 点歌执行命令 */
    var MUSIC_COMMANDS = mutableListOf<String>()

    /** 点歌冷却时间 */
    var MUSIC_COOLDOWN = 0

    /** 启用歌词 */
    var LYRIC_ENABLE = true

    /** 显示歌词翻译 */
    var LYRIC_SHOW_TRANSLATION = true

    /** 歌词颜色 */
    var LYRIC_COLOR = ""

    /** 歌词显示 BossBar */
    var LYRIC_BOSS_BAR = true

    /** 歌词显示 ActionBar */
    var LYRIC_ACTION_BAR = false

    /** 歌词显示 Title */
    var LYRIC_TITLE = false

    /** 歌词显示 聊天信息 */
    var LYRIC_CHAT = false

    /** 是否启用Hud显示 */
    var HUD_ENABLE = true

    /** Hud 信息 是否启用 */
    var HUD_INFO_ENABLE = true

    /** Hud 信息 X坐标 */
    var HUD_INFO_X = 0

    /** Hud 信息 Y坐标 */
    var HUD_INFO_Y = 0

    /** Hud 歌词 是否启用 */
    var HUD_LYRIC_ENABLE = true

    /** Hud 歌词 X坐标 */
    var HUD_LYRIC_X = 0

    /** Hud 歌词 Y坐标 */
    var HUD_LYRIC_Y = 0


    fun init() {
        try {
            load()
        } catch (e: Exception) {
            e.printStackTrace()
            disablePlugin()
        }
    }

    private fun load() {
        CHECK_UPDATE = config.getBoolean("check-update")
        LANGUAGE = config.getString("language")
        PREFIX = config.getString("prefix").colored()
        DEBUG = config.getBoolean("debug")
        API_NETEASE_LINK = config.getString("api.netease.link")
        API_NETEASE_ACCOUNT = config.getString("api.netease.account")
        API_NETEASE_PASSWORD = config.getString("api.netease.password")
        API_QQ_LINK = config.getString("api.qq.link")
        API_QQ_COOKIE = config.getString("api.qq.cookie")
        VIP_QQ = config.getString("vip.qq")
        VIP_KEY = config.getString("vip.key")
        MUSIC_COMMANDS = config.getStringList("music.commands")
        MUSIC_COOLDOWN = config.getInt("music.cooldown")
        LYRIC_ENABLE = config.getBoolean("lyric.enable")
        LYRIC_SHOW_TRANSLATION = config.getBoolean("lyric.show-translation")
        LYRIC_COLOR = config.getString("lyric.color").colored()
        LYRIC_BOSS_BAR = config.getBoolean("lyric.boss-bar")
        LYRIC_ACTION_BAR = config.getBoolean("lyric.action-bar")
        LYRIC_TITLE = config.getBoolean("lyric.title")
        LYRIC_CHAT = config.getBoolean("lyric.chat")
        HUD_ENABLE = config.getBoolean("hud.enable")
        HUD_INFO_ENABLE = config.getBoolean("hud.info.enable")
        HUD_INFO_X = config.getInt("hud.info.x")
        HUD_INFO_Y = config.getInt("hud.info.y")
        HUD_LYRIC_ENABLE = config.getBoolean("hud.lyric.enable")
        HUD_LYRIC_X = config.getInt("hud.lyric.x")
        HUD_LYRIC_Y = config.getInt("hud.lyric.y")
    }
}