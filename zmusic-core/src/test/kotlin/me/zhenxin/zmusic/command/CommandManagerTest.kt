package me.zhenxin.zmusic.command

import com.google.gson.JsonObject
import me.zhenxin.zmusic.platform.CommandRegistry
import me.zhenxin.zmusic.platform.Platform
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.PlatformLogger
import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.PluginMessageListener
import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.Scheduler
import me.zhenxin.zmusic.platform.entity.ZPlayer
import me.zhenxin.zmusic.playback.PlaybackService
import me.zhenxin.zmusic.playback.PlayerStateRegistry
import me.zhenxin.zmusic.provider.AccountBindingChallenge
import me.zhenxin.zmusic.provider.AccountBindingStatus
import me.zhenxin.zmusic.provider.MusicProviderService
import me.zhenxin.zmusic.provider.ProviderCapability
import me.zhenxin.zmusic.provider.ProviderAccount
import me.zhenxin.zmusic.provider.ProviderDescriptor
import me.zhenxin.zmusic.provider.ProviderError
import me.zhenxin.zmusic.provider.ProviderResult
import me.zhenxin.zmusic.provider.ProviderSearchResult
import me.zhenxin.zmusic.provider.ProviderTrack
import java.io.File
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * [CommandManager] 的账号、搜索和播放流程测试。
 *
 * @author 真心
 * @since 5.0.0
 */
class CommandManagerTest {
    @Test
    fun `searches provider and plays selected result token`() {
        val fixture = Fixture()

        fixture.execute("search", "--provider=netease", "起风了")
        fixture.execute("play", "1")
        fixture.execute("play", "1")

        assertEquals("netease", fixture.providers.searchedProvider)
        assertEquals("起风了", fixture.providers.searchedQuery)
        assertEquals("opaque-token", fixture.providers.playedToken)
        assertEquals(1, fixture.providers.playCalls)
        assertTrue(fixture.player.messages.any { it.contains("1. 起风了") })
        assertTrue(fixture.player.messages.any { it.contains("已向客户端发送播放请求") })
        assertTrue(fixture.player.messages.any { it.contains("搜索结果不存在或已使用") })
    }

    @Test
    fun `does not play unavailable search result`() {
        val fixture = Fixture()
        fixture.providers.track = fixture.providers.track.copy(
            playable = false,
            unavailableReason = "当前版权不可用"
        )

        fixture.execute("search", "起风了")
        fixture.execute("play", "1")
        fixture.execute("play", "1")

        assertEquals(null, fixture.providers.playedToken)
        assertEquals(2, fixture.player.messages.count { it == "当前版权不可用" })
    }

    @Test
    fun `shows browser binding challenge`() {
        val fixture = Fixture()

        fixture.execute("account", "bind", "netease")

        assertTrue(fixture.player.messages.contains("https://music.example.test/bind"))
        assertTrue(fixture.player.messages.contains("用户码：ABCD-EFGH"))
    }

    @Test
    fun `shows providers accounts and unbinds account`() {
        val fixture = Fixture()
        fixture.providers.providerList = listOf(ProviderDescriptor(
            "netease",
            "网易云音乐",
            setOf(ProviderCapability.SEARCH, ProviderCapability.PLAYBACK),
            true
        ))
        fixture.providers.accountList = listOf(ProviderAccount(
            "netease",
            "网易云音乐",
            AccountBindingStatus.BOUND,
            "测试用户"
        ))

        fixture.execute("providers")
        fixture.execute("account", "list")
        fixture.execute("account", "unbind", "netease")

        assertTrue(fixture.player.messages.any { it.contains("网易云音乐 (netease)：可用") })
        assertTrue(fixture.player.messages.any { it.contains("已绑定：测试用户") })
        assertEquals("netease", fixture.providers.unboundProvider)
        assertTrue(fixture.player.messages.contains("已解除 netease 账号绑定。"))
    }

    @Test
    fun `ignores older search response completed last`() {
        val fixture = Fixture()
        fixture.providers.deferSearch = true

        fixture.execute("search", "旧搜索")
        fixture.execute("search", "新搜索")
        fixture.providers.completeSearch(1, "new-token")
        fixture.providers.completeSearch(0, "old-token")
        fixture.execute("play", "1")

        assertEquals("new-token", fixture.providers.playedToken)
        assertTrue(fixture.player.messages.any { it.contains("1. 新搜索") })
        assertFalse(fixture.player.messages.any { it.contains("1. 旧搜索") })
    }

    @Test
    fun `reports provider failure without saving results`() {
        val fixture = Fixture()
        fixture.providers.searchFailure = ProviderError("account_required", "请先绑定账号")

        fixture.execute("search", "测试")
        fixture.execute("play", "1")

        assertFalse(fixture.player.messages.any { it.startsWith("1. ") })
        assertTrue(fixture.player.messages.any { it.contains("请先绑定账号") })
        assertTrue(fixture.player.messages.any { it.contains("搜索结果不存在或已过期") })
    }

    private class Fixture {
        val player = RecordingPlayer()
        val context = TestContext(player)
        val providers = FakeProviderService()
        private val manager = CommandManager(
            context,
            reload = { true },
            providers = providers,
            playbackService = PlaybackService(context, PlayerStateRegistry()),
            version = "test"
        )

        fun execute(vararg args: String) {
            manager.execute(player, "zmusic", args.toList())
        }
    }
}

private class FakeProviderService : MusicProviderService {
    var searchedProvider: String? = null
    var searchedQuery: String? = null
    var playedToken: String? = null
    var playCalls = 0
    var searchFailure: ProviderError? = null
    var deferSearch = false
    var providerList = emptyList<ProviderDescriptor>()
    var accountList = emptyList<ProviderAccount>()
    var unboundProvider: String? = null
    private val pendingSearches = mutableListOf<Pair<String, (ProviderResult<ProviderSearchResult>) -> Unit>>()
    var track = ProviderTrack(
        "opaque-token", "netease", "网易云音乐", "起风了", listOf("买辣椒也用券"), "起风了", true
    )

    override fun providers(player: ZPlayer, callback: (ProviderResult<List<ProviderDescriptor>>) -> Unit) =
        callback(ProviderResult.Success(providerList))

    override fun accounts(player: ZPlayer, callback: (ProviderResult<List<ProviderAccount>>) -> Unit) =
        callback(ProviderResult.Success(accountList))

    override fun beginBinding(
        player: ZPlayer,
        providerId: String,
        callback: (ProviderResult<AccountBindingChallenge>) -> Unit
    ) = callback(ProviderResult.Success(AccountBindingChallenge(
        providerId,
        "https://music.example.test/bind",
        "ABCD-EFGH",
        System.currentTimeMillis() + 60_000
    )))

    override fun unbind(player: ZPlayer, providerId: String, callback: (ProviderResult<Unit>) -> Unit) =
        callback(ProviderResult.Success(Unit)).also { unboundProvider = providerId }

    override fun search(
        player: ZPlayer,
        providerId: String?,
        query: String,
        limit: Int,
        callback: (ProviderResult<ProviderSearchResult>) -> Unit
    ) {
        searchedProvider = providerId
        searchedQuery = query
        if (deferSearch) {
            pendingSearches += query to callback
            return
        }
        val failure = searchFailure
        callback(if (failure == null) {
            ProviderResult.Success(ProviderSearchResult(query, listOf(track)))
        } else {
            ProviderResult.Failure(failure)
        })
    }

    override fun play(
        player: ZPlayer,
        trackToken: String,
        providerId: String,
        callback: (ProviderResult<Unit>) -> Unit
    ) {
        playCalls++
        playedToken = trackToken
        callback(ProviderResult.Success(Unit))
    }

    fun completeSearch(index: Int, token: String) {
        val (query, callback) = pendingSearches[index]
        callback(ProviderResult.Success(ProviderSearchResult(query, listOf(track.copy(
            trackToken = token,
            title = query
        )))))
    }
}

private class RecordingPlayer : ZPlayer {
    override val uniqueId = UUID.randomUUID()
    override val name = "Player"
    val messages = mutableListOf<String>()
    override fun hasPermission(permission: String) = true
    override fun sendMessage(message: String) {
        messages += message
    }
}

private class TestContext(private val player: ZPlayer) : PlatformContext {
    override val platform = Platform.BUKKIT
    override val platformVersion = "test"
    override val dataFolder = File("build/test-command-data")
    override val logger = object : PlatformLogger {
        override fun info(message: String) = Unit
        override fun warn(message: String) = Unit
        override fun error(message: String, throwable: Throwable?) = Unit
    }
    override val commandRegistry = CommandRegistry { }
    override val players = object : PlayerGateway {
        override fun onlinePlayers() = listOf(player)
        override fun findByName(name: String) = player.takeIf { it.name == name }
    }
    override val pluginMessenger = object : PluginMessenger {
        override fun registerChannel(channel: String, listener: PluginMessageListener) = Unit
        override fun unregisterChannel(channel: String) = Unit
        override fun send(player: ZPlayer, channel: String, payload: ByteArray) = Unit
    }
    override val scheduler = object : Scheduler {
        override fun async(task: () -> Unit) = task()
        override fun main(task: () -> Unit) = task()
    }
}
