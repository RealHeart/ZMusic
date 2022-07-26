@file:Suppress("DuplicatedCode")

package me.zhenxin.zmusic.utils

import cn.hutool.http.HttpRequest
import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.config.config
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
    var fullUrl = url
    if (params.isNotEmpty()) {
        fullUrl += "?$params"
    }
    logger.debug("Request GET: $fullUrl")
    val request = HttpRequest.get(fullUrl);
    headers.forEach {
        request.header(it.key, it.value)
    }
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
    type: PostType = PostType.JSON
): String {
    logger.debug("Request POST: $url")
    logger.debug("POST Type: ${type.name}")
    logger.debug("POST Data: $data")
    val request = HttpRequest
        .post(url)
    headers.forEach {
        request.header(it.key, it.value)
    }
    when (type) {
        PostType.JSON -> request.body(JSONObject(data).toString())
        PostType.FORM -> request.form(data.toMap())
    }

    return request(request)
}

private fun request(request: HttpRequest): String {
    if (config.PROXY_ENABLE) {
        request.setHttpProxy(config.PROXY_HOSTNAME, config.PROXY_PORT)
    }
    val res = request.execute()
    if (res.isOk) {
        if (!res.body().isNullOrEmpty()) {
            return res.body()
        }
    }
    throw ZMusicException("Http error ${res.status}, body: ${res.body()}")
}

enum class PostType() {
    JSON,
    FORM
}