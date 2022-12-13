package me.zhenxin.zmusic.config

import me.zhenxin.zmusic.utils.getStr
import me.zhenxin.zmusic.utils.supportBossBar
import taboolib.common.platform.Platform
import taboolib.common.platform.function.runningPlatform
import taboolib.module.configuration.Configuration

/**
 * 配置文件
 *
 * @author 真心
 * @since 2021/8/14 20:02
 * @email qgzhenxin@qq.com
 */

object Config {

    /** 配置文件对象 */
    @taboolib.module.configuration.Config(migrate = true)
    private lateinit var config: Configuration

    /** 检查更新 */
    val CHECK_UPDATE
        get() = config.getBoolean("check-update")

    /** 语言 */
    val LANGUAGE
        get() = config.getStr("language")

    /** 前缀 */
    val PREFIX
        get() = config.getStr("prefix")

    /** 调试模式 */
    val DEBUG
        get() = config.getBoolean("debug")

    /** 是否使用代理 */
    val PROXY_ENABLE
        get() = config.getBoolean("proxy.enable")

    /** 代理主机名 */
    val PROXY_HOSTNAME
        get() = config.getStr("proxy.hostname")

    /** 代理端口 */
    val PROXY_PORT
        get() = config.getInt("proxy.port")

    /** 网易云音乐 API链接 */
    val API_NETEASE_LINK
        get() = config.getStr("api.netease-link")

    /** ZMusic VIP QQ */
    val VIP_QQ
        get() = config.getStr("vip.qq")

    /** ZMusic VIP Key */
    val VIP_KEY
        get() = config.getStr("vip.key")

    /** 点歌执行命令 */
    val MUSIC_COMMANDS
        get() = config.getStringList("music.commands")

    /** 点歌冷却时间 */
    val MUSIC_COOLDOWN
        get() = config.getInt("music.cooldown")

    /** 启用歌词 */
    val LYRIC_ENABLE
        get() = config.getBoolean("lyric.enable")

    /** 显示歌词翻译 */
    val LYRIC_SHOW_TRANSLATION
        get() = config.getBoolean("lyric.show-translation")

    /** 歌词颜色 */
    val LYRIC_COLOR
        get() = config.getStr("lyric.color")

    /** 歌词显示 BossBar */
    val LYRIC_BOSS_BAR: Boolean
        get() {
            var result = config.getBoolean("lyric.boss-bar")
            if (runningPlatform == Platform.BUKKIT) {
                if (!supportBossBar()) result = false
            }
            return result
        }

    /** 歌词显示 ActionBar */
    val LYRIC_ACTION_BAR
        get() = config.getBoolean("lyric.action-bar")

    /** 歌词显示 Title */
    val LYRIC_TITLE
        get() = config.getBoolean("lyric.title")

    /** 歌词显示 聊天信息 */
    val LYRIC_CHAT
        get() = config.getBoolean("lyric.chat")

    /** 是否启用Hud显示 */
    val HUD_ENABLE
        get() = config.getBoolean("hud.enable")

    /** Hud 信息 是否启用 */
    val HUD_INFO_ENABLE
        get() = config.getBoolean("hud.info.enable")

    /** Hud 信息 X坐标 */
    val HUD_INFO_X
        get() = config.getInt("hud.info.x")

    /** Hud 信息 Y坐标 */
    val HUD_INFO_Y
        get() = config.getInt("hud.info.y")

    /** Hud 歌词 是否启用 */
    val HUD_LYRIC_ENABLE
        get() = config.getBoolean("hud.lyric.enable")

    /** Hud 歌词 X坐标 */
    val HUD_LYRIC_X
        get() = config.getInt("hud.lyric.x")

    /** Hud 歌词 Y坐标 */
    val HUD_LYRIC_Y
        get() = config.getInt("hud.lyric.y")


    fun reload() {
        config.reload()
    }
}