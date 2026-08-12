package me.zhenxin.zmusic.provider

import me.zhenxin.zmusic.platform.entity.ZPlayer

/**
 * Plugin 使用的远程音乐 Provider 服务。
 *
 * 实现只交换账号状态、元数据和不透明歌曲引用，不持有第三方平台密钥。
 *
 * @author 真心
 * @since 5.0.0
 */
interface MusicProviderService {
    /** @param player 请求玩家 @param callback Provider 列表回调 */
    fun providers(player: ZPlayer, callback: (ProviderResult<List<ProviderDescriptor>>) -> Unit)

    /** @param player 请求玩家 @param callback 账号状态回调 */
    fun accounts(player: ZPlayer, callback: (ProviderResult<List<ProviderAccount>>) -> Unit)

    /**
     * @param player 请求玩家
     * @param providerId Provider 标识
     * @param callback 绑定挑战回调
     */
    fun beginBinding(
        player: ZPlayer,
        providerId: String,
        callback: (ProviderResult<AccountBindingChallenge>) -> Unit
    )

    /** @param player 请求玩家 @param providerId Provider 标识 @param callback 解绑结果回调 */
    fun unbind(player: ZPlayer, providerId: String, callback: (ProviderResult<Unit>) -> Unit)

    /**
     * @param player 请求玩家
     * @param providerId 指定 Provider；null 表示搜索所有可用 Provider
     * @param query 搜索关键词
     * @param limit 最大结果数量
     * @param callback 搜索结果回调
     */
    fun search(
        player: ZPlayer,
        providerId: String?,
        query: String,
        limit: Int,
        callback: (ProviderResult<ProviderSearchResult>) -> Unit
    )

    /**
     * 让 API 解析短期歌曲引用并向玩家 Mod 投递播放资源。
     *
     * @param player 目标玩家
     * @param trackToken 搜索响应中的不透明歌曲引用
     * @param providerId 搜索结果声明的 Provider 标识
     * @param callback 投递结果回调
     */
    fun play(
        player: ZPlayer,
        trackToken: String,
        providerId: String,
        callback: (ProviderResult<Unit>) -> Unit
    )
}

/**
 * 可热替换底层连接的 Provider 服务门面。
 *
 * @author 真心
 * @since 5.0.0
 */
class MusicProviderGateway(initial: MusicProviderService) : MusicProviderService {
    @Volatile private var delegate = initial

    /** @param service 新的远程服务实现 */
    fun replace(service: MusicProviderService) {
        delegate = service
    }

    override fun providers(player: ZPlayer, callback: (ProviderResult<List<ProviderDescriptor>>) -> Unit) =
        delegate.providers(player, callback)

    override fun accounts(player: ZPlayer, callback: (ProviderResult<List<ProviderAccount>>) -> Unit) =
        delegate.accounts(player, callback)

    override fun beginBinding(
        player: ZPlayer,
        providerId: String,
        callback: (ProviderResult<AccountBindingChallenge>) -> Unit
    ) = delegate.beginBinding(player, providerId, callback)

    override fun unbind(player: ZPlayer, providerId: String, callback: (ProviderResult<Unit>) -> Unit) =
        delegate.unbind(player, providerId, callback)

    override fun search(
        player: ZPlayer,
        providerId: String?,
        query: String,
        limit: Int,
        callback: (ProviderResult<ProviderSearchResult>) -> Unit
    ) = delegate.search(player, providerId, query, limit, callback)

    override fun play(
        player: ZPlayer,
        trackToken: String,
        providerId: String,
        callback: (ProviderResult<Unit>) -> Unit
    ) = delegate.play(player, trackToken, providerId, callback)
}
