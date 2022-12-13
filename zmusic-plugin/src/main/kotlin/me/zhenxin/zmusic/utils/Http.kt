@file:Suppress("DuplicatedCode")

package me.zhenxin.zmusic.utils

import cn.hutool.http.HttpRequest
import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.exception.ZMusicException
import me.zhenxin.zmusic.logger


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
    val time = "&timestamp=${System.currentTimeMillis()}"
    var fullUrl = url
    fullUrl += if (params.isNotEmpty()) {
        "?$params$time"
    } else {
        time
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
    cookies: String = ""
): String {
    var fullUrl = url
    fullUrl += if (url.contains("?")) {
        "&timestamp=${System.currentTimeMillis()}"
    } else {
        "?timestamp=${System.currentTimeMillis()}"
    }

    val request = HttpRequest
        .post(fullUrl)

    if (cookies.isNotEmpty()) {
        request.header("cookie", cookies)
    }

    headers.forEach {
        request.header(it.key, it.value)
    }

    when (type) {
        PostType.JSON -> request.body(JSONObject(data).toString())
        PostType.FORM -> request.form(data.toMap())
    }

    logger.debug("Request POST: $fullUrl")
    logger.debug("POST Type: ${type.name}")
    logger.debug("POST Data: $data")
    return request(request)
}

private fun request(request: HttpRequest): String {
    if (Config.PROXY_ENABLE) {
        request.setHttpProxy(Config.PROXY_HOSTNAME, Config.PROXY_PORT)
    }
    val res = request.execute()
    if (res.isOk) {
        if (!res.body().isNullOrEmpty()) {
            val body = res.body()
            logger.debug("Request Body: $body")
            return body
        }
    }
    throw ZMusicException("HTTP Error ${res.status}, body: ${res.body()}")
}

enum class PostType {
    JSON,
    FORM
}