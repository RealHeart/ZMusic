package me.zhenxin.zmusic.playback

import me.zhenxin.zmusic.music.Song
import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.entity.ZPlayer

/**
 * 通过插件消息控制客户端播放。
 *
 * @property pluginMessenger 插件消息网关
 * @author 真心
 * @since 5.0.0
 */
class PlaybackService(private val pluginMessenger: PluginMessenger) {
    /**
     * 向玩家客户端发送播放指令。
     *
     * @param player 目标玩家
     * @param song 目标歌曲
     */
    fun play(player: ZPlayer, song: Song) {
        val payload = "play:${song.source}:${song.id}:${song.audioUrl.orEmpty()}".toByteArray(Charsets.UTF_8)
        pluginMessenger.send(player, DEFAULT_CHANNEL, payload)
    }

    /**
     * 向玩家客户端发送停止指令。
     *
     * @param player 目标玩家
     */
    fun stop(player: ZPlayer) {
        pluginMessenger.send(player, DEFAULT_CHANNEL, "stop".toByteArray(Charsets.UTF_8))
    }

    private companion object {
        /** 默认插件消息通道。 */
        private const val DEFAULT_CHANNEL = "zmusic:channel"
    }
}
