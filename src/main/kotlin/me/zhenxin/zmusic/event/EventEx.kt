package me.zhenxin.zmusic.event

import me.zhenxin.zmusic.taboolib.extend.sendMsg
import taboolib.common.platform.ProxyPlayer

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
    }
}