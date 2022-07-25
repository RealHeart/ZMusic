package me.zhenxin.zmusic.event

import me.zhenxin.zmusic.status.setState
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
        if (player.hasPermission("zmusic.admin.event")) {
            submit(async = true) { checkUpdate(player) }
        }
        player.setState()
    }

    fun onPlayerQuit(player: ProxyPlayer) {
        player.stopMusic()
    }
}