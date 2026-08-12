package me.zhenxin.zmusic.provider

import com.google.gson.JsonObject
import java.util.UUID
import java.util.concurrent.Delayed
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

/**
 * [PendingRequestRegistry] 的一次完成与批量清理测试。
 *
 * @author 真心
 * @since 5.0.0
 */
class PendingRequestRegistryTest {
    @Test
    fun `takes request only once`() {
        val registry = PendingRequestRegistry()
        val id = UUID.randomUUID()
        val request = request()
        registry.register(id, request)

        assertSame(request, registry.take(id, RESPONSE_TYPE))
        assertNull(registry.take(id, RESPONSE_TYPE))
    }

    @Test
    fun `drains all requests once`() {
        val registry = PendingRequestRegistry()
        repeat(3) { registry.register(UUID.randomUUID(), request()) }

        assertEquals(3, registry.drain().size)
        assertEquals(0, registry.drain().size)
    }

    @Test
    fun `removes only requests owned by player`() {
        val registry = PendingRequestRegistry()
        val firstPlayer = UUID.randomUUID()
        val secondPlayer = UUID.randomUUID()
        registry.register(UUID.randomUUID(), request(firstPlayer))
        registry.register(UUID.randomUUID(), request(secondPlayer))

        registry.removePlayer(firstPlayer)

        assertEquals(listOf(secondPlayer), registry.drain().map { it.playerId })
    }

    @Test
    fun `cancels timeout attached after fast completion`() {
        val registry = PendingRequestRegistry()
        val requestId = UUID.randomUUID()
        val request = request()
        val timeout = RecordingScheduledFuture()
        registry.register(requestId, request)

        registry.take(requestId, RESPONSE_TYPE)
        request.attachTimeout(timeout)

        assertEquals(true, timeout.cancelled)
    }

    @Test
    fun `does not consume request with wrong response type`() {
        val registry = PendingRequestRegistry()
        val requestId = UUID.randomUUID()
        val request = request()
        registry.register(requestId, request)

        kotlin.test.assertFailsWith<IllegalArgumentException> {
            registry.take(requestId, "api.account_list_result")
        }

        assertSame(request, registry.take(requestId, RESPONSE_TYPE))
    }

    private fun request(playerId: UUID = UUID.randomUUID()) =
        PendingProviderRequest(playerId, 1, RESPONSE_TYPE, {}, {})

    private companion object {
        const val RESPONSE_TYPE = "api.search_result"
    }
}

private class RecordingScheduledFuture : ScheduledFuture<Unit> {
    var cancelled = false
    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        cancelled = true
        return true
    }
    override fun isCancelled() = cancelled
    override fun isDone() = cancelled
    override fun get() = Unit
    override fun get(timeout: Long, unit: TimeUnit) = Unit
    override fun getDelay(unit: TimeUnit) = 0L
    override fun compareTo(other: Delayed) = 0
}
