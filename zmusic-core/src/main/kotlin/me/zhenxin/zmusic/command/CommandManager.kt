package me.zhenxin.zmusic.command

import me.zhenxin.zmusic.platform.PlatformCommand
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.entity.ZCommandSender
import me.zhenxin.zmusic.playback.PlaybackService
import me.zhenxin.zmusic.provider.AccountBindingStatus
import me.zhenxin.zmusic.provider.MusicProviderService
import me.zhenxin.zmusic.provider.ProviderResult
import me.zhenxin.zmusic.provider.SearchSessionRegistry

/**
 * `/zmusic` 命令处理器。
 *
 * @property context 平台上下文
 * @property reload 配置重载回调
 * @property providers 音乐 Provider 服务
 * @property playbackService 播放控制服务
 * @property searches 玩家搜索结果
 * @property version 展示给用户的版本号
 * @author 真心
 * @since 5.0.0
 */
class CommandManager(
    private val context: PlatformContext,
    private val reload: () -> Boolean,
    private val providers: MusicProviderService,
    private val playbackService: PlaybackService,
    private val version: String,
    private val searches: SearchSessionRegistry = SearchSessionRegistry()
) : PlatformCommand {
    /**
     * 分发根命令和子命令。
     *
     * @param sender 命令发送者
     * @param label 实际命令标签
     * @param args 命令参数
     * @return true 表示命令已处理
     */
    override fun execute(sender: ZCommandSender, label: String, args: List<String>): Boolean {
        if (!sender.hasPermission(Permissions.USE)) {
            sender.sendMessage("你没有权限使用 ZMusic。")
            return true
        }

        when (args.firstOrNull()?.lowercase()) {
            null, "help", "?" -> help(sender, label)
            "info" -> info(sender)
            "reload", "rl" -> reload(sender)
            "providers" -> providers(sender)
            "account" -> account(sender, args.drop(1))
            "search" -> search(sender, args.drop(1))
            "play" -> play(sender, args.drop(1))
            "stop" -> stop(sender)
            else -> sender.sendMessage("未知子命令。输入 /$label help 查看帮助。")
        }
        return true
    }

    /**
     * 返回命令补全候选。
     *
     * @param sender 命令发送者
     * @param args 当前参数
     * @return 补全候选列表
     */
    override fun suggest(sender: ZCommandSender, args: List<String>): List<String> {
        if (!sender.hasPermission(Permissions.USE)) {
            return emptyList()
        }
        if (args.size <= 1) {
            val prefix = args.firstOrNull().orEmpty()
            return ROOT_COMMANDS.filter { it.startsWith(prefix, ignoreCase = true) }
        }
        return emptyList()
    }

    /**
     * 输出帮助信息。
     *
     * @param sender 命令发送者
     * @param label 实际命令标签
     */
    private fun help(sender: ZCommandSender, label: String) {
        sender.sendMessage("ZMusic 命令：")
        sender.sendMessage("/$label info - 查看插件状态")
        sender.sendMessage("/$label reload - 重载配置")
        sender.sendMessage("/$label providers - 查看可用音乐平台")
        sender.sendMessage("/$label account - 查看音乐平台账号")
        sender.sendMessage("/$label account bind <平台> - 绑定账号")
        sender.sendMessage("/$label account unbind <平台> - 解除绑定")
        sender.sendMessage("/$label search <关键词> - 搜索音乐")
        sender.sendMessage("/$label search --provider=<平台> <关键词> - 指定平台搜索")
        sender.sendMessage("/$label play <序号> - 播放最近搜索结果")
        sender.sendMessage("/$label stop - 停止播放")
    }

    /**
     * 输出插件和平台状态。
     *
     * @param sender 命令发送者
     */
    private fun info(sender: ZCommandSender) {
        sender.sendMessage("ZMusic $version")
        sender.sendMessage("平台：${context.platform.displayName}")
        sender.sendMessage("在线玩家：${context.players.onlinePlayers().size}")
    }

    /**
     * 执行配置重载。
     *
     * @param sender 命令发送者
     */
    private fun reload(sender: ZCommandSender) {
        if (!sender.hasPermission(Permissions.RELOAD)) {
            sender.sendMessage("你没有权限重载 ZMusic。")
            return
        }
        sender.sendMessage(if (reload()) "ZMusic 已重载。" else "ZMusic 重载失败，请查看控制台。")
    }

    /** @param sender 命令发送者 */
    private fun providers(sender: ZCommandSender) = withPlayer(sender) { player ->
        sender.sendMessage("正在查询可用音乐平台...")
        providers.providers(player) { result ->
            handle(result, sender) { values ->
                if (values.isEmpty()) {
                    sender.sendMessage("当前没有可用的音乐平台。")
                } else {
                    sender.sendMessage("音乐平台：")
                    values.forEach { provider ->
                        val status = if (provider.available) "可用" else provider.unavailableReason ?: "不可用"
                        val capabilities = provider.capabilities.joinToString(",") { it.name.lowercase() }
                        sender.sendMessage("- ${provider.name} (${provider.id})：$status [$capabilities]")
                    }
                }
            }
        }
    }

    /** @param sender 命令发送者 @param args 账号命令参数 */
    private fun account(sender: ZCommandSender, args: List<String>) = withPlayer(sender) { player ->
        when (args.firstOrNull()?.lowercase()) {
            null, "list" -> {
                sender.sendMessage("正在查询音乐平台账号...")
                providers.accounts(player) { result ->
                    handle(result, sender) { accounts ->
                        if (accounts.isEmpty()) {
                            sender.sendMessage("当前没有可绑定的音乐平台。")
                        } else {
                            sender.sendMessage("音乐平台账号：")
                            accounts.forEach { account ->
                                val status = when (account.status) {
                                    AccountBindingStatus.UNBOUND -> "未绑定"
                                    AccountBindingStatus.PENDING -> "等待绑定"
                                    AccountBindingStatus.BOUND -> account.accountName?.let { "已绑定：$it" } ?: "已绑定"
                                    AccountBindingStatus.EXPIRED -> "授权已过期"
                                    AccountBindingStatus.UNAVAILABLE -> "不可用"
                                }
                                sender.sendMessage("- ${account.providerName} (${account.providerId})：$status")
                            }
                        }
                    }
                }
            }
            "bind" -> {
                val providerId = providerId(args.getOrNull(1), sender) ?: return@withPlayer
                sender.sendMessage("正在创建账号绑定请求...")
                providers.beginBinding(player, providerId) { result ->
                    handle(result, sender) { challenge ->
                        sender.sendMessage("请在浏览器中完成 ${challenge.providerId} 账号绑定：")
                        sender.sendMessage(challenge.verificationUri)
                        challenge.userCode?.let { sender.sendMessage("用户码：$it") }
                        sender.sendMessage("绑定请求将在 ${formatRemaining(challenge.expiresAt)} 后过期。")
                    }
                }
            }
            "unbind" -> {
                val providerId = providerId(args.getOrNull(1), sender) ?: return@withPlayer
                providers.unbind(player, providerId) { result ->
                    handle(result, sender) { sender.sendMessage("已解除 $providerId 账号绑定。") }
                }
            }
            else -> sender.sendMessage("用法：/zmusic account [list|bind <平台>|unbind <平台>]")
        }
    }

    /** @param sender 命令发送者 @param args 搜索参数 */
    private fun search(sender: ZCommandSender, args: List<String>) {
        withPlayer(sender) { player ->
            val providerArguments = args.filter { it.startsWith("--provider=") }
            if (providerArguments.size > 1) {
                sender.sendMessage("一次搜索只能指定一个音乐平台。")
                return@withPlayer
            }
            val providerArgument = providerArguments.singleOrNull()
            val providerId = providerArgument?.substringAfter('=')?.takeIf(PROVIDER_ID::matches)
            if (providerArgument != null && providerId == null) {
                sender.sendMessage("平台标识格式无效。")
                return@withPlayer
            }
            val keyword = args.filterNot { it.startsWith("--provider=") }.joinToString(" ").trim()
            if (keyword.isEmpty() || keyword.length > 128 || keyword.any(Char::isISOControl)) {
                sender.sendMessage("用法：/zmusic search [--provider=<平台>] <关键词>")
                return@withPlayer
            }
            sender.sendMessage("正在搜索：$keyword")
            val searchGeneration = searches.begin(player.uniqueId)
            providers.search(player, providerId, keyword, MAX_RESULTS) { result ->
                if (!searches.isCurrent(player.uniqueId, searchGeneration)) return@search
                if (result is ProviderResult.Failure) {
                    searches.discard(player.uniqueId, searchGeneration)
                }
                handle(result, sender) { searchResult ->
                    if (!searches.store(player.uniqueId, searchGeneration, searchResult)) return@handle
                    if (searchResult.tracks.isEmpty()) {
                        sender.sendMessage("没有找到歌曲：${searchResult.query}")
                    } else {
                        searchResult.tracks.forEachIndexed { index, track ->
                            val artists = track.artists.joinToString("/").ifBlank { "未知艺术家" }
                            val availability = if (track.playable) "" else " [${track.unavailableReason ?: "不可播放"}]"
                            sender.sendMessage("${index + 1}. ${track.title} - $artists · ${track.providerName}$availability")
                        }
                        sender.sendMessage("输入 /zmusic play <序号> 播放，结果 5 分钟内有效。")
                    }
                }
            }
        }
    }

    /**
     * 播放最近搜索结果中的指定歌曲。
     *
     * @param sender 命令发送者
     * @param args 搜索参数
     */
    private fun play(sender: ZCommandSender, args: List<String>) {
        withPlayer(sender) { player ->
            val index = args.singleOrNull()?.toIntOrNull()
            if (index == null) {
                sender.sendMessage("用法：/zmusic play <搜索结果序号>")
                return@withPlayer
            }
            val track = searches.find(player.uniqueId, index)
            if (track == null) {
                sender.sendMessage("搜索结果不存在或已过期，请重新搜索。")
                return@withPlayer
            }
            if (!track.playable) {
                sender.sendMessage(track.unavailableReason ?: "该歌曲当前不可播放。")
                return@withPlayer
            }
            if (searches.consume(player.uniqueId, index, track) == null) {
                sender.sendMessage("搜索结果不存在或已使用，请重新搜索。")
                return@withPlayer
            }
            sender.sendMessage("正在准备播放：${track.title}")
            providers.play(player, track.trackToken, track.providerId) { result ->
                handle(result, sender) { sender.sendMessage("已向客户端发送播放请求：${track.title}") }
            }
        }
    }

    /**
     * 发送停止播放指令。
     *
     * @param sender 命令发送者
     */
    private fun stop(sender: ZCommandSender) {
        val player = sender.asPlayer()
        if (player == null) {
            sender.sendMessage("只有玩家可以停止自己的音乐。")
            return
        }
        if (playbackService.stop(player)) {
            sender.sendMessage("已向客户端发送停止请求。")
        } else {
            sender.sendMessage("客户端 Mod 尚未完成 ZMusic 协议握手。")
        }
    }

    private inline fun withPlayer(sender: ZCommandSender, action: (me.zhenxin.zmusic.platform.entity.ZPlayer) -> Unit) {
        val player = sender.asPlayer()
        if (player == null) {
            sender.sendMessage("只有玩家可以使用此命令。")
            return
        }
        action(player)
    }

    private inline fun <T> handle(result: ProviderResult<T>, sender: ZCommandSender, success: (T) -> Unit) {
        when (result) {
            is ProviderResult.Success -> success(result.value)
            is ProviderResult.Failure -> {
                val retry = if (result.error.retryable) " 请稍后重试。" else ""
                sender.sendMessage("音乐服务请求失败：${result.error.message}$retry")
            }
        }
    }

    private fun providerId(value: String?, sender: ZCommandSender): String? {
        if (value == null || !PROVIDER_ID.matches(value)) {
            sender.sendMessage("请提供有效的平台标识，可通过 /zmusic providers 查看。")
            return null
        }
        return value
    }

    private fun formatRemaining(expiresAt: Long): String {
        val seconds = ((expiresAt - System.currentTimeMillis()).coerceAtLeast(0) + 999) / 1000
        return if (seconds >= 60) "${seconds / 60} 分钟" else "$seconds 秒"
    }

    private companion object {
        /** 根命令补全列表。 */
        private val ROOT_COMMANDS = listOf("help", "info", "reload", "providers", "account", "search", "play", "stop")
        private val PROVIDER_ID = Regex("^[a-z0-9][a-z0-9._-]{0,63}$")
        private const val MAX_RESULTS = 10
    }
}
