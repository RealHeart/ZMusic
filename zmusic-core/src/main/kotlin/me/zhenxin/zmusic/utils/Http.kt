package me.zhenxin.zmusic.utils

import com.alibaba.fastjson2.toJSONString
import me.zhenxin.zmusic.ZMusicConstants
import me.zhenxin.zmusic.exception.ZMusicException
import me.zhenxin.zmusic.logger
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * HTTP工具
 *
 * @author 真心
 * @since 2023/7/24 10:04
 * @email qgzhenxin@qq.com
 */
val client = OkHttpClient()

/**
 * GET获取
 * @param url 链接
 * @param paramsMap 参数(Map)
 */
fun httpGet(
    url: String,
    paramsMap: Map<String, String> = mapOf(),
    headers: Map<String, String> = mapOf(),
    cache: Boolean = false
): String {
    val httpUrl = buildUrl(url, paramsMap, cache)
    val request = Request.Builder().url(httpUrl)
    addHeaders(request, headers)

    return request(request)
}

/**
 * POST获取
 * @param url 连接
 * @param data 参数
 */
fun httpPost(
    url: String,
    data: Map<String, Any?> = mapOf(),
    headers: Map<String, String> = mapOf(),
    type: PostType = PostType.JSON,
    cache: Boolean = false
): String {
    val httpUrl = buildUrl(url, cache = cache)
    val request = Request.Builder().url(httpUrl)
    addHeaders(request, headers)

    when (type) {
        PostType.JSON -> {
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = data.toJSONString().toRequestBody(mediaType)
            request.post(body)
        }

        PostType.FORM -> {
            val from = FormBody.Builder()
            data.forEach {
                from.add(it.key, it.value.toString())
            }
            val body = from.build()
            request.post(body)
        }
    }

    return request(request)
}

private fun buildUrl(url: String, paramsMap: Map<String, String> = mapOf(), cache: Boolean): HttpUrl {
    val builder = url.toHttpUrl().newBuilder()
    paramsMap.map {
        builder.addQueryParameter(it.key, it.value)
    }
    if (cache) {
        builder.addQueryParameter("timestamp", System.currentTimeMillis().toString())
    }
    return builder.build()
}

private fun addHeaders(request: Request.Builder, headers: Map<String, String>) {
    headers.forEach {
        request.addHeader(it.key, it.value)
    }
}

private fun request(builder: Request.Builder): String {
    builder.addHeader("User-Agent", "ZMusic/${ZMusicConstants.PLUGIN_VERSION}")
    val request = builder.build()

    logger.debug("HTTP Request: ${request.method} ${request.url}")
    if (request.body != null) {
        logger.debug("Request Body: ${request.body}")
    }

    val call = client.newCall(request)
    try {
        val res = call.execute()
        if (res.isSuccessful) {
            val body = res.body?.string()
            logger.debug("Response Body: $body")
            return body ?: throw ZMusicException("response body is null")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        throw ZMusicException("request error: ${e.message}")
    }
    return ""
}

enum class PostType {
    JSON,
    FORM
}