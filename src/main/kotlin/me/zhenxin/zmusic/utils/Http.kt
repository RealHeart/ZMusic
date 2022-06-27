@file:Suppress("DuplicatedCode")

package me.zhenxin.zmusic.utils

import com.alibaba.fastjson2.JSON.toJSONString
import me.zhenxin.zmusic.config.config
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

private lateinit var client: OkHttpClient
private val mediaType = "application/json; charset=utf-8".toMediaType()
private val urlencoded = "application/x-www-form-urlencoded; charset=utf-8".toMediaType()

/**
 * GET获取
 * @param url 链接
 * @param paramsMap 参数(Map)
 * @return 字符串
 */
fun get(
    url: String,
    paramsMap: MutableMap<String, Any?> = mutableMapOf(),
    headers: MutableMap<String, String> = mutableMapOf()
): String {
    val params = paramsMap.map { "${it.key}=${it.value}" }.joinToString("&")
    var fullUrl = url
    if (params.isNotEmpty()) {
        fullUrl += "?$params"
    }
    logger.debug("Request GET: $fullUrl")
    val req = Request.Builder()
        .url(fullUrl)
        .get()
    headers.forEach {
        req.addHeader(it.key, it.value)
    }
    return call(req.build())
}

/**
 * POST获取
 * @param url 连接
 * @param data 参数
 */
fun post(
    url: String,
    data: MutableMap<String, Any?> = mutableMapOf(),
    headers: MutableMap<String, String> = mutableMapOf()
): String {
    val json = toJSONString(data)
    logger.debug("Request POST: $url")
    logger.debug("POST Data: $json")

    val body = json.toRequestBody(mediaType)
    val req = Request.Builder()
        .url(url)
        .post(body)
    headers.forEach {
        req.addHeader(it.key, it.value)
    }
    return call(req.build())
}

/**
 * POST获取
 * @param url 链接
 * @param params 参数
 * @return 字符串
 */
fun post(
    url: String,
    params: String,
    headers: MutableMap<String, String> = mutableMapOf()
): String {
    logger.debug("Request POST: $url")
    logger.debug("POST Data: $params")
    val body = params.toRequestBody(urlencoded)
    val req = Request.Builder()
        .url(url)
        .post(body)
    headers.forEach {
        req.addHeader(it.key, it.value)
    }
    return call(req.build())
}

private fun call(request: Request): String {
    val res = client.newCall(request).execute()
    if (res.isSuccessful) {
        if (res.body != null) {
            return res.body!!.string()
        }
    }
    throw ZMusicException("Http error ${res.code}, body: ${res.body?.string()}")
}

fun initHttpClient() {
    val builder = OkHttpClient().newBuilder()
    if (config.PROXY_ENABLE) {
        builder.proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(config.PROXY_HOSTNAME, config.PROXY_PORT)))
    }
    client = builder.build()
}