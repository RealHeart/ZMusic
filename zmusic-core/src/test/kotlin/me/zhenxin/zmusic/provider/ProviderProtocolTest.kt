package me.zhenxin.zmusic.provider

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * [ProviderProtocol] 的外部响应边界测试。
 *
 * @author 真心
 * @since 5.0.0
 */
class ProviderProtocolTest {
    @Test
    fun `parses explicit provider error`() {
        val result = ProviderProtocol.search(JsonObject().apply {
            addProperty("status", "error")
            addProperty("code", "account_required")
            addProperty("message", "请先绑定账号")
            addProperty("retryable", false)
        })

        val failure = assertIs<ProviderResult.Failure>(result)
        assertEquals("account_required", failure.error.code)
    }

    @Test
    fun `rejects non https binding address`() {
        val data = JsonObject().apply {
            addProperty("status", "ok")
            addProperty("providerId", "netease")
            addProperty("verificationUri", "http://example.com/bind")
            addProperty("expiresAt", System.currentTimeMillis() + 60_000)
        }

        assertFailsWith<IllegalArgumentException> { ProviderProtocol.binding(data) }
    }

    @Test
    fun `rejects too many search results received from api`() {
        val tracks = JsonArray().apply {
            repeat(25) { index ->
                add(JsonObject().apply {
                    addProperty("trackToken", "token-$index")
                    addProperty("providerId", "netease")
                    addProperty("providerName", "网易云音乐")
                    addProperty("title", "歌曲 $index")
                    add("artists", JsonArray().apply { add("歌手") })
                    addProperty("playable", true)
                })
            }
        }
        val data = JsonObject().apply {
            addProperty("status", "ok")
            addProperty("query", "测试")
            add("tracks", tracks)
        }

        assertFailsWith<IllegalArgumentException> { ProviderProtocol.search(data) }
    }

    @Test
    fun `rejects invalid provider id`() {
        val data = JsonObject().apply {
            addProperty("status", "ok")
            add("providers", JsonArray().apply {
                add(JsonObject().apply {
                    addProperty("id", "NetEase Music")
                    addProperty("name", "网易云音乐")
                    add("capabilities", JsonArray())
                    addProperty("available", true)
                })
            })
        }

        assertFailsWith<IllegalArgumentException> { ProviderProtocol.providers(data) }
    }

    @Test
    fun `rejects string boolean`() {
        val data = JsonObject().apply {
            addProperty("status", "error")
            addProperty("code", "provider_timeout")
            addProperty("message", "上游超时")
            addProperty("retryable", "true")
        }

        assertFailsWith<IllegalArgumentException> { ProviderProtocol.search(data) }
    }

    @Test
    fun `parses provider list and account status`() {
        val providers = ProviderProtocol.providers(JsonObject().apply {
            addProperty("status", "ok")
            add("providers", JsonArray().apply {
                add(JsonObject().apply {
                    addProperty("id", "netease")
                    addProperty("name", "网易云音乐")
                    add("capabilities", JsonArray().apply { add("search"); add("playback") })
                    addProperty("available", true)
                })
            })
        })
        val accounts = ProviderProtocol.accounts(JsonObject().apply {
            addProperty("status", "ok")
            add("accounts", JsonArray().apply {
                add(JsonObject().apply {
                    addProperty("providerId", "netease")
                    addProperty("providerName", "网易云音乐")
                    addProperty("status", "bound")
                    addProperty("accountName", "测试用户")
                })
            })
        })

        val provider = assertIs<ProviderResult.Success<List<ProviderDescriptor>>>(providers).value.single()
        assertEquals(setOf(ProviderCapability.SEARCH, ProviderCapability.PLAYBACK), provider.capabilities)
        val account = assertIs<ProviderResult.Success<List<ProviderAccount>>>(accounts).value.single()
        assertEquals(AccountBindingStatus.BOUND, account.status)
        assertEquals("测试用户", account.accountName)
    }

    @Test
    fun `rejects control characters in display message`() {
        val data = JsonObject().apply {
            addProperty("status", "error")
            addProperty("code", "internal_error")
            addProperty("message", "失败\n伪造消息")
        }

        assertFailsWith<IllegalArgumentException> { ProviderProtocol.unit(data) }
    }
}
