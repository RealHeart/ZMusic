package me.zhenxin.zmusic.provider

import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * 解析 Plugin 与 API 之间的 Provider 业务消息。
 *
 * @author 真心
 * @since 5.0.0
 */
internal object ProviderProtocol {
    /** @param data Provider 列表响应 @return 解析结果 */
    fun providers(data: JsonObject): ProviderResult<List<ProviderDescriptor>> = result(data) {
        requiredArray(data, "providers", MAX_PROVIDERS).map { element ->
            val item = element.asJsonObject
            ProviderDescriptor(
                id = requiredProviderId(item, "id"),
                name = requiredString(item, "name", 128),
                capabilities = requiredArray(item, "capabilities").map { capability ->
                    require(capability.isJsonPrimitive && capability.asJsonPrimitive.isString) {
                        "Invalid capability"
                    }
                    ProviderCapability.valueOf(capability.asString.uppercase())
                }.toSet(),
                available = optionalBoolean(item, "available") ?: false,
                unavailableReason = optionalString(item, "unavailableReason", 256)
            )
        }
    }

    /** @param data 账号列表响应 @return 解析结果 */
    fun accounts(data: JsonObject): ProviderResult<List<ProviderAccount>> = result(data) {
        requiredArray(data, "accounts", MAX_ACCOUNTS).map { element ->
            val item = element.asJsonObject
            ProviderAccount(
                providerId = requiredProviderId(item, "providerId"),
                providerName = requiredString(item, "providerName", 128),
                status = AccountBindingStatus.valueOf(requiredString(item, "status", 32).uppercase()),
                accountName = optionalString(item, "accountName", 128)
            )
        }
    }

    /** @param data 账号绑定响应 @return 解析结果 */
    fun binding(data: JsonObject): ProviderResult<AccountBindingChallenge> = result(data) {
        AccountBindingChallenge(
            providerId = requiredProviderId(data, "providerId"),
            verificationUri = requiredHttpsUrl(data, "verificationUri"),
            userCode = optionalString(data, "userCode", 64),
            expiresAt = requiredPositiveLong(data, "expiresAt").also {
                require(it > System.currentTimeMillis()) { "Binding challenge has expired" }
            }
        )
    }

    /** @param data 无响应体操作结果 @return 解析结果 */
    fun unit(data: JsonObject): ProviderResult<Unit> {
        if (status(data) == "error") {
            return ProviderResult.Failure(error(data))
        }
        require(status(data) == "ok") { "Invalid Provider response status" }
        return ProviderResult.Success(Unit)
    }

    /** @param data Provider 播放响应 @return 解析结果 */
    fun playback(data: JsonObject): ProviderResult<JsonObject> = result(data) {
        requiredObject(data, "playback")
    }

    /** @param data 搜索响应 @return 解析结果 */
    fun search(data: JsonObject): ProviderResult<ProviderSearchResult> = result(data) {
        val tracks = requiredArray(data, "tracks", MAX_SEARCH_RESULTS).map { element ->
            val item = element.asJsonObject
            ProviderTrack(
                trackToken = requiredString(item, "trackToken", 2048),
                providerId = requiredProviderId(item, "providerId"),
                providerName = requiredString(item, "providerName", 128),
                title = requiredString(item, "title", 256),
                artists = requiredArray(item, "artists", MAX_ARTISTS).map { artist ->
                    require(artist.isJsonPrimitive && artist.asJsonPrimitive.isString) { "Invalid artist" }
                    artist.asString.takeIf {
                        it.isNotBlank() && it.length <= 128 && it.none(Char::isISOControl)
                    }
                        ?: throw IllegalArgumentException("Invalid artist")
                },
                album = optionalString(item, "album", 256),
                playable = optionalBoolean(item, "playable") ?: false,
                unavailableReason = optionalString(item, "unavailableReason", 256)
            )
        }
        ProviderSearchResult(requiredString(data, "query", 128), tracks)
    }

    private inline fun <T> result(data: JsonObject, value: () -> T): ProviderResult<T> {
        if (status(data) == "error") {
            return ProviderResult.Failure(error(data))
        }
        require(status(data) == "ok") { "Invalid Provider response status" }
        return ProviderResult.Success(value())
    }

    private fun status(data: JsonObject) = requiredString(data, "status", 16)

    private fun error(data: JsonObject) = ProviderError(
        code = requiredString(data, "code", 64),
        message = requiredString(data, "message", 256),
        retryable = optionalBoolean(data, "retryable") ?: false
    )

    private fun requiredArray(data: JsonObject, name: String, maximumSize: Int? = null): JsonArray {
        val value = data.get(name)
        require(value != null && value.isJsonArray) { "Missing $name" }
        require(maximumSize == null || value.asJsonArray.size() <= maximumSize) { "$name has too many items" }
        return value.asJsonArray
    }

    private fun requiredObject(data: JsonObject, name: String): JsonObject {
        val value = data.get(name)
        require(value != null && value.isJsonObject) { "Missing $name" }
        return value.asJsonObject
    }

    private fun requiredString(data: JsonObject, name: String, maximumLength: Int): String {
        val value = optionalString(data, name, maximumLength)
        require(!value.isNullOrBlank()) { "Missing $name" }
        return value
    }

    private fun requiredProviderId(data: JsonObject, name: String): String {
        val value = requiredString(data, name, 64)
        require(PROVIDER_ID.matches(value)) { "Invalid $name" }
        return value
    }

    private fun optionalBoolean(data: JsonObject, name: String): Boolean? {
        val value = data.get(name) ?: return null
        if (value.isJsonNull) return null
        require(value.isJsonPrimitive && value.asJsonPrimitive.isBoolean) { "Invalid $name" }
        return value.asBoolean
    }

    private fun optionalString(data: JsonObject, name: String, maximumLength: Int): String? {
        val value = data.get(name) ?: return null
        if (value.isJsonNull) return null
        require(value.isJsonPrimitive && value.asJsonPrimitive.isString) { "Invalid $name" }
        return value.asString.takeIf { text ->
            text.length <= maximumLength && text.none(Char::isISOControl)
        }
            ?: throw IllegalArgumentException("Invalid $name")
    }

    private fun requiredHttpsUrl(data: JsonObject, name: String): String {
        val value = requiredString(data, name, 2048)
        val uri = runCatching { java.net.URI(value) }.getOrNull()
        require(uri != null && uri.scheme.equals("https", ignoreCase = true) &&
            !uri.host.isNullOrBlank() && uri.userInfo == null) { "Invalid $name" }
        return value
    }

    private fun requiredPositiveLong(data: JsonObject, name: String): Long {
        val primitive = data.get(name)?.takeIf { it.isJsonPrimitive }?.asJsonPrimitive
            ?: throw IllegalArgumentException("Missing $name")
        require(primitive.isNumber) { "Invalid $name" }
        val value = runCatching { primitive.asBigDecimal.longValueExact() }
            .getOrElse { throw IllegalArgumentException("Invalid $name", it) }
        require(value > 0) { "Invalid $name" }
        return value
    }

    private val PROVIDER_ID = Regex("^[a-z0-9][a-z0-9._-]{0,63}$")
    private const val MAX_PROVIDERS = 50
    private const val MAX_ACCOUNTS = 50
    private const val MAX_SEARCH_RESULTS = 20
    private const val MAX_ARTISTS = 10
}
