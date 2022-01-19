package me.zhenxin.zmusic.utils

import com.alibaba.fastjson.JSON.toJSONString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


/**
 * HTTP请求工具
 * @author 真心
 * @since 2021/1/24 14:58
 * @email qgzhenxin@qq.com
 */

private val client = OkHttpClient().newBuilder().build()
private val mediaType = "application/json; charset=utf-8".toMediaType()

/**
 * HTTP请求返回
 */
data class HttpResult(
    /* 状态码 */
    val code: Int,
    /* 数据 */
    val data: String
)

/**
 * GET获取
 * @param url 链接
 * @param paramsMap 参数(Map)
 * @return 字符串
 */
fun httpGet(
    url: String,
    paramsMap: MutableMap<String, Any?> = mutableMapOf(),
    headers: MutableMap<String, String> = mutableMapOf()
): HttpResult {
    val params = paramsMap.map { "${it.key}=${it.value}" }.joinToString("&")
    var fullUrl = url
    if (params.isNotEmpty()) {
        fullUrl += "?$params"
    }
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
 * @param paramsMap 参数
 */
fun httpPost(
    url: String,
    paramsMap: MutableMap<String, Any?> = mutableMapOf(),
    headers: MutableMap<String, String> = mutableMapOf()
): HttpResult {
    val body = toJSONString(paramsMap).toRequestBody(mediaType)
    val req = Request.Builder()
        .url(url)
        .post(body)
    headers.forEach {
        req.addHeader(it.key, it.value)
    }
    return call(req.build())
}

private fun call(request: Request): HttpResult {
    val res = client.newCall(request).execute()
    if (res.isSuccessful) {
        if (res.body != null) {
            return HttpResult(
                res.code,
                res.body!!.string()
            )
        }
    }
    return HttpResult(400, "{}")
}