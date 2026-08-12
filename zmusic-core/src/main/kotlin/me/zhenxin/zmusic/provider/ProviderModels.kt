package me.zhenxin.zmusic.provider

/**
 * 音乐 Provider 对外公开的能力。
 *
 * @author 真心
 * @since 5.0.0
 */
enum class ProviderCapability {
    SEARCH,
    PLAYBACK,
    ACCOUNT_BINDING,
    LYRICS
}

/**
 * API 返回的音乐 Provider 描述。
 *
 * @property id 稳定 Provider 标识
 * @property name 展示名称
 * @property capabilities 已获授权且当前可用的能力
 * @property available 当前是否可用
 * @property unavailableReason 不可用原因
 * @author 真心
 * @since 5.0.0
 */
data class ProviderDescriptor(
    val id: String,
    val name: String,
    val capabilities: Set<ProviderCapability>,
    val available: Boolean,
    val unavailableReason: String? = null
)

/**
 * 玩家在一个 Provider 上的账号绑定状态。
 *
 * @author 真心
 * @since 5.0.0
 */
enum class AccountBindingStatus {
    UNBOUND,
    PENDING,
    BOUND,
    EXPIRED,
    UNAVAILABLE
}

/**
 * 玩家音乐平台账号状态。
 *
 * @property providerId Provider 标识
 * @property providerName Provider 展示名称
 * @property status 绑定状态
 * @property accountName 已绑定账号展示名
 * @author 真心
 * @since 5.0.0
 */
data class ProviderAccount(
    val providerId: String,
    val providerName: String,
    val status: AccountBindingStatus,
    val accountName: String? = null
)

/**
 * 浏览器账号绑定挑战。
 *
 * @property providerId Provider 标识
 * @property verificationUri 官方或 ZMusic 账号绑定地址
 * @property userCode 设备授权码，可为空
 * @property expiresAt Unix 毫秒过期时间
 * @author 真心
 * @since 5.0.0
 */
data class AccountBindingChallenge(
    val providerId: String,
    val verificationUri: String,
    val userCode: String?,
    val expiresAt: Long
)

/**
 * 不包含播放地址的音乐搜索结果。
 *
 * @property trackToken API 签发的不透明短期歌曲引用
 * @property providerId Provider 标识
 * @property providerName Provider 展示名称
 * @property title 歌曲标题
 * @property artists 艺术家列表
 * @property album 专辑名
 * @property playable 当前是否允许播放
 * @property unavailableReason 不可播放原因
 * @author 真心
 * @since 5.0.0
 */
data class ProviderTrack(
    val trackToken: String,
    val providerId: String,
    val providerName: String,
    val title: String,
    val artists: List<String>,
    val album: String?,
    val playable: Boolean,
    val unavailableReason: String? = null
)

/**
 * 一次音乐搜索响应。
 *
 * @property query 原始关键词
 * @property tracks 搜索结果
 * @author 真心
 * @since 5.0.0
 */
data class ProviderSearchResult(
    val query: String,
    val tracks: List<ProviderTrack>
)

/**
 * Provider 请求失败信息。
 *
 * @property code 稳定错误码
 * @property message 可展示错误消息
 * @property retryable 是否适合稍后重试
 * @author 真心
 * @since 5.0.0
 */
data class ProviderError(
    val code: String,
    val message: String,
    val retryable: Boolean = false
)

/**
 * Provider 异步操作结果。
 *
 * @author 真心
 * @since 5.0.0
 */
sealed class ProviderResult<out T> {
    /** @property value 成功结果 */
    data class Success<T>(val value: T) : ProviderResult<T>()

    /** @property error 失败详情 */
    data class Failure(val error: ProviderError) : ProviderResult<Nothing>()
}
