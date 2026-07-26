package me.zhenxin.zmusic.bukkit

import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.PlayerListener
import me.zhenxin.zmusic.platform.entity.ZPlayer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * Bukkit 玩家查询实现。
 *
 * @author 真心
 * @since 5.0.0
 */
class BukkitPlayerGateway(private val plugin: JavaPlugin) : PlayerGateway, Listener {
    private var listener: PlayerListener? = null

    /**
     * 返回 Bukkit 当前在线玩家。
     *
     * @return 在线玩家集合
     */
    override fun onlinePlayers(): Collection<ZPlayer> {
        return Bukkit.getOnlinePlayers().map(::BukkitPlayer)
    }

    /**
     * 按精确名称查询 Bukkit 玩家。
     *
     * @param name 玩家名
     * @return 找到的玩家；不存在时返回 null
     */
    override fun findByName(name: String): ZPlayer? {
        return Bukkit.getPlayerExact(name)?.let(::BukkitPlayer)
    }

    /** 注册 Bukkit 玩家上下线监听。 */
    override fun registerListener(listener: PlayerListener) {
        if (this.listener == null) {
            plugin.server.pluginManager.registerEvents(this, plugin)
        }
        this.listener = listener
    }

    /** 注销 Bukkit 玩家上下线监听。 */
    override fun unregisterListener() {
        listener = null
        HandlerList.unregisterAll(this)
    }

    /** @param event 玩家登录事件 */
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        listener?.onJoin(BukkitPlayer(event.player))
    }

    /** @param event 玩家退出事件 */
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        listener?.onQuit(BukkitPlayer(event.player))
    }
}
