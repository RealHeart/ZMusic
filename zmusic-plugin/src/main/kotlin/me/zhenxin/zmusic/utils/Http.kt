@file:Suppress("DuplicatedCode")

package me.zhenxin.zmusic.utils

import cn.hutool.http.HttpRequest
import cn.hutool.json.JSONObject
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
    headers: Map<String, String> = mutableMapOf()
): String {
    val params = paramsMap.map { "${it.key}=${it.value}" }.joinToString("&")
    var fullUrl = url
    if (params.isNotEmpty()) {
        fullUrl += "?$params"
    }

    val request = HttpRequest.get(fullUrl)
    headers.forEach {
        request.header(it.key, it.value)
    }

    logger.debug("Request GET: $fullUrl")
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
): String {
    val request = HttpRequest.post(url)

    headers.forEach {
        request.header(it.key, it.value)
    }

    when (type) {
        PostType.JSON -> request.body(JSONObject(data).toString())
        PostType.FORM -> request.form(data.toMap())
    }

    logger.debug("Request POST: $url")
    logger.debug("POST Type: ${type.name}")
    logger.debug("POST Data: $data")
    return request(request)
}

@Suppress("HttpUrlsUsage")
private fun request(request: HttpRequest): String {
    logger.debug("Proxy Enable: ${Config.PROXY_ENABLE}")
    if (Config.PROXY_ENABLE) {
        val type = Config.PROXY_TYPE
        val host = Config.PROXY_HOSTNAME
        val port = Config.PROXY_PORT
        val address = InetSocketAddress(host, port)
        val proxy = Proxy(type, address)
        logger.debug("Use $type Proxy: $address")
        request.setProxy(proxy)
    }
    try {
        val res = request.execute()
        if (res.isOk) {
            if (!res.body().isNullOrEmpty()) {
                val body = res.body()
                logger.debug("Request Body: $body")
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