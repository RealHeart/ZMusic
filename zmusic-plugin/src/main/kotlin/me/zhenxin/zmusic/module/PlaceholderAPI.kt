package me.zhenxin.zmusic.module

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.status.getState
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer

/**
 * PAPI HOOK
 *
 * @author 真心
 * @since 2022/7/31 11:14
 * @email qgzhenxin@qq.com
 */
class PlaceholderAPI : PlaceholderExpansion() {
    override fun getIdentifier(): String = "zmusic"

    override fun getAuthor(): String = "ZhenXin"

    override fun getVersion(): String = ZMusic.VERSION_NAME

    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        if (player != null) {
            return when (params) {
                "status_name" -> {
                    val name = adaptPlayer(player).getState().name
                    name ?: "None"
                }

                "status_singer" -> {
                    val singer = adaptPlayer(player).getState().singer
                    singer ?: "None"
                }

                "status_lyric" -> {
                    val lyric = adaptPlayer(player).getState().lyric
                    lyric ?: "None"
                }

                "status_current_time" -> {
                    val currentTime = adaptPlayer(player).getState().currentTime
                    formatTime(currentTime)
                }

                "status_max_time" -> {
                    val maxTime = adaptPlayer(player).getState().time
                    formatTime(maxTime)
                }

                else -> null
            }
        }
        return null
    }

    private fun formatTime(time: Int?): String {
        return if (time != null) {
            if (time < 60) {
                "00" + ":" + String.format("%02d", time)
            } else if (time < 3600) {
                val m = time / 60
                val s = time % 60
                String.format("%02d", m) + ":" + String.format("%02d", s)
            } else {
                val h = time / 3600
                val m = time % 3600 / 60
                val s = time % 3600 % 60
                String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s)
            }
        } else {
            "00:00"
        }
    }
}