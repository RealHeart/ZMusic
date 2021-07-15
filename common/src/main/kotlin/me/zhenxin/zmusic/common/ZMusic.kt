package me.zhenxin.zmusic.common

import me.zhenxin.zmusic.common.config.Config
import me.zhenxin.zmusic.common.i18n.I18n
import java.io.File

/**
 * 主入口
 *
 * @author 真心
 * @since 2021/7/15 1:33
 * @email qgzhenxin@qq.com
 */
object ZMusic {
    fun onEnable(dataFolder: File) {
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }
        Config.load(dataFolder)
        I18n.load(dataFolder)
        if (Config.Debug) {
            println("Version: ${Config.Version}")
            println("CheckUpdate: ${Config.CheckUpdate}")
            println("Prefix: ${Config.Prefix}")
            println("Language: ${Config.Language}")
            println("Debug: ${Config.Debug}")

            println("ApiLinkNetease: ${Config.ApiLinkNetease}")
            println("ApiLinkQQ: ${Config.ApiLinkQQ}")
            println("ApiAccountNeteaseAccount: ${Config.ApiAccountNeteaseAccount}")
            println("ApiAccountNeteasePassword: ${Config.ApiAccountNeteasePassword}")
            println("ApiAccountQQCookie: ${Config.ApiAccountQQCookie}")

            println("VipQQ: ${Config.VipQQ}")
            println("VipKey: ${Config.VipKey}")

            println("MusicCommands: ${Config.MusicCommands}")
            println("MusicCoolDown: ${Config.MusicCoolDown}")

            println("LyricEnabled: ${Config.LyricEnabled}")
            println("LyricShowLyricTranslation${Config.LyricShowLyricTranslation}")
            println("LyricBossBar: ${Config.LyricBossBar}")
            println("LyricActionBar: ${Config.LyricActionBar}")
            println("LyricTitle${Config.LyricTitle}")
            println("LyricChat: ${Config.LyricChat}")

            println("HudEnabled: ${Config.HudEnabled}")
            println("HudInfoEnabled: ${Config.HudInfoEnabled}")
            println("HudInfoX: ${Config.HudInfoX}")
            println("HudInfoY: ${Config.HudInfoY}")
            println("HudLyricEnabled: ${Config.HudLyricEnabled}")
            println("HudLyricX: ${Config.HudLyricX}")
            println("HudLyricY: ${Config.HudLyricY}")
        }
    }
}