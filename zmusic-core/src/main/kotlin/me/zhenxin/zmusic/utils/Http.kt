package me.zhenxin.zmusic.utils


import me.zhenxin.zmusic.ZMusicConstants
import me.zhenxin.zmusic.exception.ZMusicException
import me.zhenxin.zmusic.logger
import org.dromara.hutool.core.net.url.UrlBuilder
import org.dromara.hutool.http.HttpUtil
import org.dromara.hutool.http.client.Request
import org.dromara.hutool.http.meta.Method
import org.dromara.hutool.json.JSONObject

/**
 * HTTP工具
 *
 * @author 真心
 * @since 2023/7/24 10:04
 * @email qgzhenxin@qq.com
 */

/**
 * GET获取
 * @param url 链接
 * @param paramsMap 参数(Map)
 */
fun httpGet(
    url: String,
    params: Map<String, String> = mapOf(),
    headers: Map<String, String> = mapOf(),
    cache: Boolean = true
): String {
    val httpUrl = buildUrl(url, cache, params)
    val request = HttpUtil.createGet(httpUrl)
    return request(request, headers)
}

/**
 * POST获取
 * @param url 连接
 * @param data 参数
 */
fun httpPost(
    url: String,
    data: JSONObject,
    headers: Map<String, String> = mapOf(),
    cache: Boolean = true
): String {
    val httpUrl = buildUrl(url, cache)
    val request = HttpUtil.createPost(httpUrl).body(data.toString())
    return request(request, headers)
}

private fun buildUrl(url: String, cache: Boolean, params: Map<String, String> = mapOf()): String {
    val builder = UrlBuilder.ofHttp(url)
    params.map {
        builder.addQuery(it.key, it.value)
    }
    if (!cache) {
        builder.addQuery("t", System.currentTimeMillis())
    }
    return builder.build()
}

private fun request(request: Request, headers: Map<String, String> = mapOf()): String {
    request.header("User-Agent", "ZMusic/${ZMusicConstants.PLUGIN_VERSION}")
    headers.forEach {
        request.header(it.key, it.value)
    }
    logger.debug("Request: ${request.method()} ${request.url()}")
    if (request.method() == Method.POST) {
        logger.debug("Request body: ${request.body()}")
    }
    val response = request.send()
    val body = response.body().string
    if (!response.isOk) {
        logger.error("Request failed: ${response.status}, body: $body")
        throw ZMusicException("Request failed: ${response.status}, body: $body")
    }
    logger.debug("Response: $body")
    return body
}