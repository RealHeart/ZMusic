package me.zhenxin.zmusic.api

import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.utils.playMusic
import me.zhenxin.zmusic.utils.stopMusic
import taboolib.common.platform.function.adaptPlayer

/**
 * ZMusic API
 *
 * @author 真心
 * @since 2021/8/5 12:41
 * @email qgzhenxin@qq.com
 */
@Suppress("unused")
object ZMusicApi {
    @JvmStatic
    val version: String = ZMusic.VERSION_NAME

    /**
     * 播放音乐
     * @param player 玩家
     * @param url 音乐地址
     */
    @JvmStatic
    fun playMusic(player: org.bukkit.entity.Player, url: String) {
        adaptPlayer(player).playMusic(url)
    }

    /**
     * 播放音乐
     * @param player 玩家
     * @param url 音乐地址
     */
    @JvmStatic
    fun playMusic(player: com.velocitypowered.api.proxy.Player, url: String) {
        adaptPlayer(player).playMusic(url)
    }

    /**
     * 播放音乐
     * @param player 玩家
     * @param url 音乐地址
     */
    @JvmStatic
    fun playMusic(player: net.md_5.bungee.api.connection.ProxiedPlayer, url: String) {
        adaptPlayer(player).playMusic(url)
    }


    /**
     * 播放音乐
     * @param player 玩家
     */
    @JvmStatic
    fun stopMusic(player: org.bukkit.entity.Player) {
        adaptPlayer(player).stopMusic()
    }

    /**
     * 播放音乐
     * @param player 玩家
     */
    @JvmStatic
    fun stopMusic(player: com.velocitypowered.api.proxy.Player) {
        adaptPlayer(player).stopMusic()
    }

    /**
     * 播放音乐
     * @param player 玩家
     */
    @JvmStatic
    fun stopMusic(player: net.md_5.bungee.api.connection.ProxiedPlayer) {
        adaptPlayer(player).stopMusic()
    }

}