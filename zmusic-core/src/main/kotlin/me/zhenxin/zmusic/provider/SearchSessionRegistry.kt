package me.zhenxin.zmusic.provider

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * 保存玩家最近一次搜索结果，供 `/zmusic play <序号>` 使用。
 *
 * @property ttlMillis 搜索结果有效期
 * @property clock 当前时间来源
 * @author 真心
 * @since 5.0.0
 */
class SearchSessionRegistry(
    private val ttlMillis: Long = 5 * 60 * 1000L,
    private val clock: () -> Long = { System.nanoTime() / 1_000_000L }
) {
    private val sessions = ConcurrentHashMap<UUID, SearchSession>()
    private val sequence = AtomicLong()

    /**
     * 开始一次搜索并立即使该玩家之前的结果失效。
     *
     * @param playerId 玩家 UUID
     * @return 搜索序号
     */
    fun begin(playerId: UUID): Long {
        val generation = sequence.incrementAndGet()
        sessions[playerId] = SearchSession(generation, clock(), emptyList(), false)
        return generation
    }

    /**
     * 仅保存该玩家最新一次搜索的响应。
     *
     * @param playerId 玩家 UUID
     * @param generation [begin] 返回的搜索序号
     * @param result 搜索结果
     * @return true 表示响应属于最新搜索并已保存
     */
    fun store(playerId: UUID, generation: Long, result: ProviderSearchResult): Boolean {
        var stored = false
        sessions.computeIfPresent(playerId) { _, session ->
            if (session.generation == generation) {
                stored = true
                SearchSession(generation, clock(), result.tracks, true)
            } else {
                session
            }
        }
        return stored
    }

    /** @param playerId 玩家 UUID @param generation 搜索序号 @return 是否仍是最新搜索 */
    fun isCurrent(playerId: UUID, generation: Long): Boolean {
        return sessions[playerId]?.generation == generation
    }

    /**
     * 清理失败的搜索，但不影响该玩家随后发起的新搜索。
     *
     * @param playerId 玩家 UUID
     * @param generation 搜索序号
     */
    fun discard(playerId: UUID, generation: Long) {
        sessions.computeIfPresent(playerId) { _, session ->
            session.takeUnless { it.generation == generation }
        }
    }

    /**
     * @param playerId 玩家 UUID
     * @param index 从 1 开始的展示序号
     * @return 有效搜索结果；不存在、过期或越界时返回 null
     */
    fun find(playerId: UUID, index: Int): ProviderTrack? {
        if (index < 1) return null
        val session = sessions[playerId] ?: return null
        if (!session.ready) return null
        if (clock() - session.createdAt >= ttlMillis) {
            sessions.remove(playerId, session)
            return null
        }
        return session.tracks.getOrNull(index - 1)
    }

    /**
     * 原子消费一个一次性搜索结果。
     *
     * @param playerId 玩家 UUID
     * @param index 从 1 开始的展示序号
     * @param expected 读取阶段得到的预期曲目
     * @return 未消费且仍有效的搜索结果
     */
    fun consume(playerId: UUID, index: Int, expected: ProviderTrack): ProviderTrack? {
        if (index < 1) return null
        var track: ProviderTrack? = null
        sessions.computeIfPresent(playerId) { _, session ->
            if (!session.ready || clock() - session.createdAt >= ttlMillis || index > session.tracks.size) {
                null
            } else if (session.tracks[index - 1] != expected) {
                session
            } else if (index in session.consumedIndexes) {
                session
            } else {
                track = session.tracks[index - 1]
                session.copy(consumedIndexes = session.consumedIndexes + index)
            }
        }
        return track
    }

    /** @param playerId 玩家 UUID */
    fun remove(playerId: UUID) {
        sessions.remove(playerId)
    }

    /** 清理全部搜索结果。 */
    fun clear() = sessions.clear()
}

private data class SearchSession(
    val generation: Long,
    val createdAt: Long,
    val tracks: List<ProviderTrack>,
    val ready: Boolean,
    val consumedIndexes: Set<Int> = emptySet()
)
