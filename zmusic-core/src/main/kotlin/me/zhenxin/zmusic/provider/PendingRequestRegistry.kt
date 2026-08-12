package me.zhenxin.zmusic.provider

import com.google.gson.JsonObject
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ScheduledFuture

/**
 * 关联 Plugin 发出的 Provider 请求与 API 响应。
 *
 * @author 真心
 * @since 5.0.0
 */
internal class PendingRequestRegistry {
    private val requests = ConcurrentHashMap<UUID, PendingProviderRequest>()

    /** @param requestId 请求 UUID @param request 待完成请求 */
    fun register(requestId: UUID, request: PendingProviderRequest) {
        check(requests.putIfAbsent(requestId, request) == null) { "Duplicate requestId" }
    }

    /**
     * 仅当响应类型与请求匹配时消费请求。
     *
     * @param requestId 请求 UUID
     * @param responseType 实际响应类型
     * @return 已移除请求；未知请求返回 null
     */
    fun take(requestId: UUID, responseType: String): PendingProviderRequest? {
        val request = requests[requestId] ?: return null
        require(request.expectedResponseType == responseType) { "Unexpected response type for requestId" }
        return request.takeIf { requests.remove(requestId, request) }?.also { it.cancelTimeout() }
    }

    /** @param requestId 请求 UUID @param request 预期请求 @return 是否成功移除 */
    fun remove(requestId: UUID, request: PendingProviderRequest): Boolean {
        val removed = requests.remove(requestId, request)
        if (removed) request.cancelTimeout()
        return removed
    }

    /** @return 原子移除的所有挂起请求 */
    fun drain(): List<PendingProviderRequest> {
        return requests.entries.mapNotNull { (requestId, request) ->
            request.takeIf { remove(requestId, request) }
        }
    }

    /** @param playerId 已离线玩家 UUID */
    fun removePlayer(playerId: UUID) {
        requests.entries.forEach { (requestId, request) ->
            if (request.playerId == playerId) remove(requestId, request)
        }
    }
}

/**
 * 单个 Provider 请求的完成和失败回调。
 *
 * @property playerId 发起请求的玩家 UUID
 * @property playerSession 发起请求时的玩家连接序号
 * @property expectedResponseType 该请求唯一允许的响应消息类型
 * @property complete 成功响应处理器
 * @property fail 失败处理器
 * @author 真心
 * @since 5.0.0
 */
internal class PendingProviderRequest(
    val playerId: UUID,
    val playerSession: Long,
    val expectedResponseType: String,
    val complete: (JsonObject) -> Unit,
    val fail: (ProviderResult.Failure) -> Unit
) {
    private var timeout: ScheduledFuture<*>? = null
    private var completed = false

    /** @param future 请求超时任务 */
    @Synchronized
    fun attachTimeout(future: ScheduledFuture<*>) {
        if (completed) {
            future.cancel(false)
        } else {
            timeout = future
        }
    }

    /** 取消请求超时任务。 */
    @Synchronized
    fun cancelTimeout() {
        completed = true
        timeout?.cancel(false)
        timeout = null
    }
}
