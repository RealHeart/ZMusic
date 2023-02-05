@file:Suppress("DuplicatedCode")

package me.zhenxin.zmusic.utils

import com.alibaba.fastjson2.toJSONString
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.exception.ZMusicException
import me.zhenxin.zmusic.logger
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
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

    val request = Request.Builder().get().url(fullUrl)
    headers.forEach {
        request.header(it.key, it.value)
    }

    logger.debug("Request GET: $fullUrl")
    return request(request.build())
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
    cookies: String = ""
): String {
    val request = Request.Builder()

    if (cookies.isNotEmpty()) {
        request.header("cookie", cookies)
    }

    headers.forEach {
        request.header(it.key, it.value)
    }

    val json = data.toJSONString()
    val mediaType = "application/json".toMediaType()
    val body = json.toRequestBody(mediaType)
    request.post(body).url(url)
    logger.debug("Request POST: $url")
    logger.debug("POST Data: $data")
    return request(request.build())
}

private fun request(request: Request): String {
    proxy()
    val call = client.newCall(request)
    val response = call.execute()
    if (response.isSuccessful) {
        val body = response.body
        if (body != null) {
            val string = body.string()
            body.close()
            response.close()
            if (string.isNotEmpty()) {
                logger.debug("Request Body: $string")
                return string
            }
        }
    }
    throw ZMusicException("HTTP Error ${response.code}, body: ${response.body?.string()}")
}

private fun proxy() {
    if (Config.PROXY_ENABLE) {
        val type = Proxy.Type.HTTP
        val host = Config.PROXY_HOSTNAME
        val port = Config.PROXY_PORT
        val address = InetSocketAddress(host, port)
        val proxy = Proxy(type, address)
        client = OkHttpClient()
            .newBuilder()
            .proxy(proxy)
            .build()
    }
}

private var client = OkHttpClient()