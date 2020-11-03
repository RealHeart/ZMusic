package me.zhenxin.zmusic

import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.module.Logger
import me.zhenxin.zmusic.module.Player
import me.zhenxin.zmusic.module.Sender
import me.zhenxin.zmusic.module.Tasker
import java.io.File
import java.net.CookieHandler
import java.net.CookieManager

object ZMusic {
    var isBC = false
    var plugin: Any? = null
    var logger: Logger? = null
    var tasker: Tasker? = null
    var sender: Sender? = null
    var player: Player? = null

    var dataFolder: File? = null
    var thisVer: String? = null
    var thisVerCode = 202009190
    var bilibiliIsVIP = false
    var isViaVer = true
    var isEnable: Boolean = true
    var isEnabled: Boolean = false

    fun enable() {
        logger?.normal(Lang.Loading.loading)
        val manager = CookieManager()
        CookieHandler.setDefault(manager)
        Config.load()
        logger?.normal(Lang.Loading.loaded)
    }
}
