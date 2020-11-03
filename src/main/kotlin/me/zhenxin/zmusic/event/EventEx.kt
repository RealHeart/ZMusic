package me.zhenxin.zmusic.event

import me.zhenxin.zmusic.ZMusic


object EventEx {
    fun onJoin(player: Any) {
        // 检查更新
        ZMusic.logger?.normal(player.toString())
    }
}
