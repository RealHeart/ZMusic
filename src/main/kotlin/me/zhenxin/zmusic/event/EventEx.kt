package me.zhenxin.zmusic.event

import me.zhenxin.zmusic.taboolib.extend.sendMsg
import me.zhenxin.zmusic.utils.checkUpdate
import me.zhenxin.zmusic.utils.stopMusic
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit

/**
 * 统一事件执行器
 *
 * @author 真心
 * @since 2021/9/27 21:13
 * @email qgzhenxin@qq.com
 */
object EventEx {
    fun onPlayerJoin(player: ProxyPlayer) {
        player.sendMsg("进入服务器事件测试")
        if (player.isOp) {
            submit(async = true) { checkUpdate(player) }
        }
    }

    fun onPlayerQuit(player: ProxyPlayer) {
        player.stopMusic()
    }
}