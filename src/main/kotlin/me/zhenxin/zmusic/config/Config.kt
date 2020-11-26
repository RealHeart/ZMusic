package me.zhenxin.zmusic.config

import me.zhenxin.zmusic.ZMusic
import org.yaml.snakeyaml.Yaml

object Config {

    var version: Int = 0
    var update: Boolean = false
    var prefix: String = "§bZMusic §e>>> §r"
    var debug: Boolean = false

    object Api {
        var netease: String = "https://netease.api.zhenxin.xyz"
        var qq: String = "https://qqmusic.api.zhenxin.xyz"
        val xima = "https://api.ximalaya.com"
    }

    object Account {
        object Netease {
            var loginType: String = "phone"
            var account: String = "18888888888"
            var password: String = "a123456"
            var passwordType: String = "normal"
            var follow: Boolean = true
        }

        object BiliBili {
            var qq: String = "1307993674"
            var key: String = "none"
        }
    }

    object Music {
        var money: Int = 10
        var cooldown: Int = 5
    }

    object Lyric {
        var enable: Boolean = true
        var showLyricTr: Boolean = true
        var color: String = "&b"
        var bossBar: Boolean = true
        var actionBar: Boolean = false
        var subTitle: Boolean = false
        var chatMessage: Boolean = false

        object Hud {
            var enable: Boolean = true
            var infoX: Int = 2
            var infoY: Int = 12
            var lyricX: Int = 2
            var lyricY: Int = 72
        }
    }

    fun load() {
        var temp: Any
        val yaml = Yaml()
        val inputStream = this.javaClass.classLoader
            .getResourceAsStream("config.yml")
        val config: Map<String, Any> = yaml.load(inputStream)
        version = config["version"] as Int
        update = config["update"] as Boolean
        prefix = (config["prefix"] as String).replace("&", "§")
        debug = config["debug"] as Boolean

        temp = config["api"] as Map<*, *>
        Api.netease = temp["netease"] as String
        Api.qq = temp["qq"] as String

        temp = config["account"] as Map<*, *>
        temp = temp["netease"] as Map<*, *>
        Account.Netease.loginType = temp["loginType"] as String
        Account.Netease.account = temp["account"] as String
        Account.Netease.password = temp["password"] as String
        Account.Netease.passwordType = temp["passwordType"] as String
        Account.Netease.follow = temp["follow"] as Boolean
        temp = config["account"] as Map<*, *>
        temp = temp["bilibili"] as Map<*, *>
        Account.BiliBili.qq = temp["qq"] as String
        Account.BiliBili.key = temp["key"] as String

        temp = config["music"] as Map<*, *>
        Music.money = temp["money"] as Int
        Music.money = temp["cooldown"] as Int

        temp = config["lyric"] as Map<*, *>
        Lyric.enable = temp["enable"] as Boolean
        Lyric.color = (temp["color"] as String).replace("&", "§")
        Lyric.bossBar = temp["bossBar"] as Boolean
        Lyric.actionBar = temp["actionBar"] as Boolean
        Lyric.subTitle = temp["subTitle"] as Boolean
        Lyric.chatMessage = temp["chatMessage"] as Boolean
        temp = temp["hud"] as Map<*, *>
        Lyric.Hud.enable = temp["enable"] as Boolean
        Lyric.Hud.infoX = temp["infoX"] as Int
        Lyric.Hud.infoY = temp["infoY"] as Int
        Lyric.Hud.lyricX = temp["lyricX"] as Int
        Lyric.Hud.lyricY = temp["lyricY"] as Int

        ZMusic.logger.log(Lang.Loading.configLoaded)
    }
}
