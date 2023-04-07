@file:Suppress("DuplicatedCode")

package me.zhenxin.zmusic.utils

import cn.hutool.core.net.url.UrlBuilder
import cn.hutool.http.Header
import cn.hutool.http.HttpRequest
import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.ZMusicData
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.exception.ZMusicException
import me.zhenxin.zmusic.logger
import java.net.InetSocketAddress
import java.net.Proxy


/**
 * HTTP请求工具
 * @author 真心
 * @since 2021/1/24 14:58
 * @email qgzhenxin@qq.com
 */

/**
 * GET获取
 * @param url 链接
 * @param paramsMap 参数(Map)
 */
fun get(
    url: String,
    paramsMap: Map<String, Any?> = mutableMapOf(),
    headers: Map<String, String> = mutableMapOf(),
    disableCache: Boolean = false
): String {
    val ub = UrlBuilder.of(url)
    paramsMap.map { ub.addQuery(it.key, it.value) }
    if (disableCache) {
        ub.addQuery("timestamp", System.currentTimeMillis())
    }
    val u = ub.build()

    val request = HttpRequest.get(u)
    headers.forEach {
        request.header(it.key, it.value)
    }

    logger.debug("Request GET: $u")
    return request(request)
}

/**
 * POST获取
 * @param url 连接
 * @param data 参数
 */
fun post(
    url: String,
    data: Map<String, Any?> = mutableMapOf(),
    headers: Map<String, String> = mutableMapOf(),
    type: PostType = PostType.JSON,
    disableCache: Boolean = false
): String {
    val ub = UrlBuilder.of(url)
    if (disableCache) {
        ub.addQuery("timestamp", System.currentTimeMillis())
    }
    val u = ub.build()

    val request = HttpRequest.post(u)

    headers.forEach {
        request.header(it.key, it.value)
    }

    when (type) {
        PostType.JSON -> request.body(JSONObject(data).toString())
        PostType.FORM -> request.form(data.toMap())
    }

    logger.debug("Request POST: $u")
    logger.debug("POST Type: ${type.name}")
    logger.debug("POST Data: $data")
    return request(request)
}

private fun request(request: HttpRequest): String {
    if (Config.PROXY_ENABLE) {
        val type = Config.PROXY_TYPE
        val host = Config.PROXY_HOSTNAME
        val port = Config.PROXY_PORT
        val address = InetSocketAddress(host, port)
        val proxy = Proxy(type, address)
        logger.debug("Use $type Proxy: $address")
        request.setProxy(proxy)
    }

    request.header(Header.USER_AGENT, "ZMusic/${ZMusicData.VERSION_NAME}")

    try {
        val res = request.execute()
        if (res.isOk) {
            if (!res.body().isNullOrEmpty()) {
                val body = res.body()
                logger.debug("Response Body: $body")
                return body
            }
        }
        throw ZMusicException("HTTP Exception ${res.status}, body: ${res.body()}")
    } catch (e: Exception) {
        e.printStackTrace()
        throw ZMusicException("HTTP Exception, ${e.message}")
    }
}

enum class PostType {
    JSON,
    FORM
}