package me.zhenxin.zmusic.event

import me.zhenxin.zmusic.ZMusic


internal object EventEx {
    fun onJoin(player: Any) {
        // 检查更新
        ZMusic.logger?.log(player.toString())
    }
}
