package me.zhenxin.zmusic.playback

import com.google.gson.JsonObject
import me.zhenxin.zmusic.platform.entity.ZPlayer
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * Plugin 进程内的玩家 Mod 能力和当前播放状态表。
 *
 * @author 真心
 * @since 5.0.0
 */
class PlayerStateRegistry {
    private val states = ConcurrentHashMap<UUID, PlayerState>()

    /**
     * 返回玩家状态，不存在时以当前时间创建未握手状态。
     *
     * @param player 在线玩家
     * @return 当前状态
     */
    fun state(player: ZPlayer): PlayerState {
        return states.computeIfAbsent(player.uniqueId) { PlayerState(System.currentTimeMillis()) }
    }

    /**
     * 记录 Mod 握手数据。
     *
     * @param player 在线玩家
     * @param hello `client.hello.data`
     */
    fun markReady(player: ZPlayer, hello: JsonObject) {
        state(player).apply {
            hasMod = true
            modVersion = hello.string("modVersion")
            minecraftVersion = hello.string("minecraftVersion")
            loader = hello.string("loader")
        }
    }

    /**
     * 记录已向玩家发送的播放请求和歌曲元数据。
     *
     * @param player 目标玩家
     * @param requestId 播放请求 UUID
     * @param song 歌曲 JSON
     */
    fun markRequested(player: ZPlayer, requestId: String, song: JsonObject) {
        state(player).apply {
            currentRequestId = requestId
            currentSong = song.deepCopy()
        }
    }

    /**
     * 更新播放状态；终态清除当前请求。
     *
     * @param player 目标玩家
     * @param playback 归一化播放状态
     */
    fun updatePlayback(player: ZPlayer, playback: JsonObject) {
        state(player).apply {
            when (playback.string("state")) {
                "stopped", "ended", "failed" -> {
                    this.playback = null
                    currentRequestId = ""
                    currentSong = null
                }
                else -> this.playback = playback.deepCopy()
            }
        }
    }

    /**
     * 清理已离线玩家，防止代理端长时间持有旧状态。
     *
     * @param onlinePlayers 当前平台在线玩家集合
     */
    fun retainOnline(onlinePlayers: Collection<ZPlayer>) {
        val online = onlinePlayers.mapTo(hashSetOf()) { it.uniqueId }
        states.keys.removeIf { it !in online }
        onlinePlayers.forEach(::state)
    }

    /**
     * 清理单个离线玩家的易失状态。
     *
     * @param player 已离线玩家
     */
    fun remove(player: ZPlayer) {
        states.remove(player.uniqueId)
    }

    /**
     * 清理全部进程内状态。
     */
    fun clear() = states.clear()
}

/**
 * 单个玩家的易失通信状态。
 *
 * @property connectedAt Plugin 首次观察到玩家的时间
 * @author 真心
 * @since 5.0.0
 */
class PlayerState(val connectedAt: Long) {
    @Volatile var hasMod: Boolean = false
    @Volatile var modVersion: String = ""
    @Volatile var minecraftVersion: String = ""
    @Volatile var loader: String = ""
    @Volatile var currentRequestId: String = ""
    @Volatile var currentSong: JsonObject? = null
    @Volatile var playback: JsonObject? = null
    private val seenMessages = LinkedHashSet<String>()
    private val rateWindows = mutableMapOf<String, MessageRateWindow>()

    /** 在单个玩家连接内去重最近 256 条消息。 */
    @Synchronized
    internal fun rememberMessage(id: String): Boolean {
        if (!seenMessages.add(id)) return false
        if (seenMessages.size > 256) seenMessages.remove(seenMessages.first())
        return true
    }

    /** 按包协议约定限制单个玩家的消息频率。 */
    @Synchronized
    internal fun allowMessage(type: String, now: Long = System.currentTimeMillis()): Boolean {
        val (duration, maximum) = when (type) {
            "client.hello" -> 10_000L to 3
            "client.status", "client.error" -> 1_000L to 5
            "client.progress" -> 1_000L to 2
            else -> return true
        }
        val window = rateWindows.getOrPut(type) { MessageRateWindow(now, 0) }
        if (now - window.startedAt >= duration) {
            window.startedAt = now
            window.count = 1
            return true
        }
        if (window.count >= maximum) return false
        window.count++
        return true
    }
}

private class MessageRateWindow(var startedAt: Long, var count: Int)

internal fun JsonObject.string(name: String): String {
    val element = get(name)
    return if (element == null || element.isJsonNull) "" else element.asString
}
