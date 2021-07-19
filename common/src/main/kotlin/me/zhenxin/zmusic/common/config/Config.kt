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

    // 版本
    var Version = 0
    // 是否检查更新
    var CheckUpdate = true
    // 语言
    var Language = ""
    // 是否开启调试模式
    var Debug = false

    // API设置

    // 网易云接口地址
    var ApiLinkNetease = ""
    // QQ音乐接口地址
    var ApiLinkQQ = ""
    // 网易云账号
    var ApiAccountNeteaseAccount = ""
    // 网易云密码
    var ApiAccountNeteasePassword = ""
    // QQ音乐 Cookie
    var ApiAccountQQCookie = ""

    // VIP 设置

    // 授权QQ
    var VipQQ = ""
    // 授权Key
    var VipKey = ""

    // 点歌设置

    // 点歌后执行命令
    var MusicCommands = mutableListOf<String>()
    // 点歌冷却
    var MusicCoolDown = 0

    // 歌词设置

    // 是否启用歌词
    var LyricEnabled = true
    // 是否显示歌词翻译
    var LyricShowLyricTranslation = true
    // 歌词颜色
    var LyricColor = ""
    // 是否使用BossBar显示歌词
    var LyricBossBar = true
    // 是否使用ActionBar显示歌词
    var LyricActionBar = false
    // 是否使用Title显示歌词
    var LyricTitle = false
    // 是否使用聊天信息显示歌词
    var LyricChat = false

    // Hud设置

    // 是否启用Hud
    var HudEnabled = true
    // 是否显示信息
    var HudInfoEnabled = true
    // 信息X坐标
    var HudInfoX = 0
    // 信息Y坐标
    var HudInfoY = 0
    // 以下同上 对应歌词
    var HudLyricEnabled = true
    var HudLyricX = 0
    var HudLyricY = 0

    /**
     * 加载配置文件
     *
     * @param dataFolder 数据目录
     */
    fun load(dataFolder: File) {
        var temp: Any
        val yaml = Yaml()
        val file = File(dataFolder, "/config.yml")
        if (!file.exists()) {
            val tmp = this.javaClass.classLoader.getResourceAsStream("config.yml")
            tmp?.saveData(file.absolutePath)
        }
        val config: LinkedHashMap<String, Any> = yaml.load(file.inputStream())

        // 基本设置
        Version = config["version"] as Int
        CheckUpdate = config["check-update"] as Boolean
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
