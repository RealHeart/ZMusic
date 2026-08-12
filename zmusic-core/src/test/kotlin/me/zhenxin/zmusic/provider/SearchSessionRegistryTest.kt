package me.zhenxin.zmusic.provider

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import java.util.Collections
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * [SearchSessionRegistry] 的玩家隔离和过期测试。
 *
 * @author 真心
 * @since 5.0.0
 */
class SearchSessionRegistryTest {
    @Test
    fun `isolates search results by player`() {
        val registry = SearchSessionRegistry()
        val first = UUID.randomUUID()
        val second = UUID.randomUUID()
        registry.store(first, registry.begin(first), result("first"))
        registry.store(second, registry.begin(second), result("second"))

        assertEquals("first", registry.find(first, 1)?.trackToken)
        assertEquals("second", registry.find(second, 1)?.trackToken)
        assertNull(registry.find(first, 2))
    }

    @Test
    fun `expires old search result`() {
        var now = 1_000L
        val player = UUID.randomUUID()
        val registry = SearchSessionRegistry(ttlMillis = 100, clock = { now })
        registry.store(player, registry.begin(player), result("token"))

        now = 1_100L

        assertNull(registry.find(player, 1))
    }

    @Test
    fun `ignores response from older search`() {
        val registry = SearchSessionRegistry()
        val player = UUID.randomUUID()
        val older = registry.begin(player)
        val newer = registry.begin(player)

        assertEquals(false, registry.store(player, older, result("older")))
        assertEquals(true, registry.store(player, newer, result("newer")))
        assertEquals("newer", registry.find(player, 1)?.trackToken)
    }

    @Test
    fun `discard removes only matching search`() {
        val registry = SearchSessionRegistry()
        val player = UUID.randomUUID()
        val older = registry.begin(player)
        val newer = registry.begin(player)

        registry.discard(player, older)

        assertEquals(true, registry.isCurrent(player, newer))
        registry.discard(player, newer)
        assertEquals(false, registry.isCurrent(player, newer))
    }

    @Test
    fun `concurrent consume returns track once`() {
        val registry = SearchSessionRegistry()
        val player = UUID.randomUUID()
        registry.store(player, registry.begin(player), result("once"))
        val ready = CountDownLatch(2)
        val start = CountDownLatch(1)
        val results = Collections.synchronizedList(mutableListOf<String?>())
        val executor = Executors.newFixedThreadPool(2)
        try {
            repeat(2) {
                executor.submit {
                    ready.countDown()
                    start.await()
                    val expected = registry.find(player, 1)!!
                    results += registry.consume(player, 1, expected)?.trackToken
                }
            }
            assertEquals(true, ready.await(5, TimeUnit.SECONDS))
            start.countDown()
            executor.shutdown()
            assertEquals(true, executor.awaitTermination(5, TimeUnit.SECONDS))
        } finally {
            executor.shutdownNow()
        }

        assertEquals(listOf(null, "once"), results.sortedBy { it.orEmpty() })
    }

    @Test
    fun `does not consume replacement search using stale track`() {
        val registry = SearchSessionRegistry()
        val player = UUID.randomUUID()
        registry.store(player, registry.begin(player), result("old"))
        val stale = registry.find(player, 1)!!
        registry.store(player, registry.begin(player), result("new"))

        assertNull(registry.consume(player, 1, stale))
        val current = registry.find(player, 1)!!
        assertEquals("new", registry.consume(player, 1, current)?.trackToken)
    }

    private fun result(token: String) = ProviderSearchResult("query", listOf(
        ProviderTrack(token, "netease", "网易云音乐", "歌曲", listOf("歌手"), null, true)
    ))
}
