package me.zhenxin.zmusic.command

import me.zhenxin.zmusic.music.MusicCatalog
import me.zhenxin.zmusic.platform.PlatformCommand
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.entity.ZCommandSender
import me.zhenxin.zmusic.playback.PlaybackService

/**
 * `/zmusic` 命令处理器。
 *
 * @property context 平台上下文
 * @property reload 配置重载回调
 * @property musicCatalog 音乐搜索服务
 * @property playbackService 播放控制服务
 * @property version 展示给用户的版本号
 * @author 真心
 * @since 5.0.0
 */
class CommandManager(
    private val context: PlatformContext,
    private val reload: () -> Boolean,
    private val musicCatalog: MusicCatalog,
    private val playbackService: PlaybackService,
    private val version: String
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
        sender.sendMessage("/$label search <关键词> - 搜索音乐")
        sender.sendMessage("/$label play <关键词> - 播放音乐")
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

    /**
     * 搜索音乐并输出前几条结果。
     *
     * @param sender 命令发送者
     * @param args 搜索参数
     */
    private fun search(sender: ZCommandSender, args: List<String>) {
        val keyword = args.joinToString(" ").trim()
        if (keyword.isEmpty()) {
            sender.sendMessage("用法：/zmusic search <关键词>")
            return
        }
        val results = musicCatalog.search(keyword, 5)
        if (results.isEmpty()) {
            sender.sendMessage("没有找到歌曲：$keyword")
            return
        }
        results.forEachIndexed { index, song ->
            sender.sendMessage("${index + 1}. ${song.title} - ${song.artists.joinToString("/")}")
        }
    }

    /**
     * 搜索第一条结果并发送播放指令。
     *
     * @param sender 命令发送者
     * @param args 搜索参数
     */
    private fun play(sender: ZCommandSender, args: List<String>) {
        val player = sender.asPlayer()
        if (player == null) {
            sender.sendMessage("只有玩家可以播放音乐。")
            return
        }
        val keyword = args.joinToString(" ").trim()
        if (keyword.isEmpty()) {
            sender.sendMessage("用法：/zmusic play <关键词>")
            return
        }
        val song = musicCatalog.search(keyword, 1).firstOrNull()
        if (song == null) {
            sender.sendMessage("没有找到歌曲：$keyword")
            return
        }
        playbackService.play(player, song)
        sender.sendMessage("正在播放：${song.title}")
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
        playbackService.stop(player)
        sender.sendMessage("已停止播放。")
    }

    private companion object {
        /** 根命令补全列表。 */
        private val ROOT_COMMANDS = listOf("help", "info", "reload", "search", "play", "stop")
    }
}
