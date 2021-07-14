package me.zhenxin.zmusic.common.config

import me.zhenxin.zmusic.common.utils.saveData
import org.yaml.snakeyaml.Yaml
import java.io.File

/**
 * 配置项
 *
 * @author 真心
 * @since 2021/7/14 22:22
 * @email qgzhenxin@qq.com
 */
object Config {
    // 基本设置
    var Version = 0
    var CheckUpdate = true
    var Prefix = ""
    var Language = ""
    var Debug = false

    // API设置
    var ApiLinkNetease = ""
    var ApiLinkQQ = ""
    var ApiAccountNeteaseAccount = ""
    var ApiAccountNeteasePassword = ""
    var ApiAccountQQCookie = ""

    // VIP 设置
    var VipQQ = ""
    var VipKey = ""

    // 点歌设置
    var MusicCommands = mutableListOf<String>()
    var MusicCoolDown = 0

    // 歌词设置
    var LyricEnabled = true
    var LyricShowLyricTranslation = true
    var LyricColor = ""
    var LyricBossBar = true
    var LyricActionBar = false
    var LyricTitle = false
    var LyricChat = false

    // Hud设置
    var HudEnabled = true
    var HudInfoEnabled = true
    var HudInfoX = 0
    var HudInfoY = 0
    var HudLyricEnabled = true
    var HudLyricX = 0
    var HudLyricY = 0

    /**
     * 加载配置文件
     *
     * @param dataDir 数据目录
     */
    fun load(dataFolder: File) {
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }
        var temp: Any
        val yaml = Yaml()
        var file = File(dataFolder, "/config.yml")
        if (!file.exists()) {
            val tmp = this.javaClass.classLoader.getResourceAsStream("config.yml")
            tmp?.saveData(file.absolutePath)
            file = File(dataFolder, "/config.yml")
        }
        val config: LinkedHashMap<String, Any> = yaml.load(file.inputStream())

        // 基本设置
        Version = config["version"] as Int
        CheckUpdate = config["check-update"] as Boolean
        Prefix = config["prefix"] as String
        Prefix = Prefix.replace("&", "§")
        Language = config["language"] as String
        Debug = config["debug"] as Boolean

        // API设置
        temp = config["api"] as LinkedHashMap<*, *>
        temp = temp["link"] as LinkedHashMap<*, *>
        ApiLinkNetease = temp["netease"] as String
        ApiLinkQQ = temp["qq"] as String

        temp = config["api"] as LinkedHashMap<*, *>
        temp = temp["account"] as LinkedHashMap<*, *>
        temp = temp["netease"] as LinkedHashMap<*, *>
        ApiAccountNeteaseAccount = temp["account"] as String
        ApiAccountNeteasePassword = temp["password"] as String

        temp = config["api"] as LinkedHashMap<*, *>
        temp = temp["account"] as LinkedHashMap<*, *>
        temp = temp["qq"] as LinkedHashMap<*, *>
        ApiAccountQQCookie = temp["cookie"] as String

        // VIP设置
        temp = config["vip"] as LinkedHashMap<*, *>
        VipQQ = temp["qq"] as String
        VipKey = temp["key"] as String

        // 点歌设置
        temp = config["music"] as LinkedHashMap<*, *>
        MusicCommands = temp["commands"] as MutableList<String>
        MusicCoolDown = temp["cool-down"] as Int

        // 歌词设置
        temp = config["lyric"] as LinkedHashMap<*, *>
        LyricEnabled = temp["enabled"] as Boolean
        LyricShowLyricTranslation = temp["show-lyric-translation"] as Boolean
        LyricColor = temp["color"] as String
        LyricColor = LyricColor.replace("&", "§")
        LyricBossBar = temp["boss-bar"] as Boolean
        LyricActionBar = temp["action-bar"] as Boolean
        LyricTitle = temp["title"] as Boolean
        LyricChat = temp["chat"] as Boolean

        // Hud设置
        temp = config["hud"] as LinkedHashMap<*, *>
        HudEnabled = temp["enabled"] as Boolean

        temp = temp["info"] as LinkedHashMap<*, *>
        HudInfoEnabled = temp["enabled"] as Boolean
        HudInfoX = temp["x"] as Int
        HudInfoY = temp["y"] as Int

        temp = config["hud"] as LinkedHashMap<*, *>
        temp = temp["lyric"] as LinkedHashMap<*, *>
        HudLyricEnabled = temp["enabled"] as Boolean
        HudLyricX = temp["x"] as Int
        HudLyricY = temp["y"] as Int
    }
}
