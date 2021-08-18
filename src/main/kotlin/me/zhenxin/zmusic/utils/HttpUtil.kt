package me.zhenxin.zmusic.utils

import com.alibaba.fastjson.JSON
import me.zhenxin.zmusic.exception.ZMusicException
import okhttp3.Call
import okhttp3.FormBody
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
object HttpUtil {

    /**
     * HTTP请求返回
     */
    data class HttpResult(
        /* 状态码 */
        val code: Int,
        /* 数据 */
        val data: Any?
    )

    // OkHttp客户端
    private val client = OkHttpClient().newBuilder().build()

    /**
     * GET获取
     * @param url 链接
     * @param params 参数
     * @return 字符串
     */
    @JvmStatic
    fun get(url: String, params: MutableMap<String, Any?> = mutableMapOf()): HttpResult {
        var getUrl = url
        if (!getUrl.contains('?')) {
            getUrl = "${getUrl}?"
        }
        params.forEach {
            getUrl = "${getUrl}${it.key}=${it.value}&"
        }
        getUrl = getUrl.substring(0, getUrl.length - 1)
        val req = Request.Builder().url(getUrl).build()
        return call(client.newCall(req))
    }

    /**
     * POST获取
     * @param url 连接
     * @param params 参数
     * @return 字符串
     */
    @JvmStatic
    fun post(
        url: String,
        params: MutableMap<String, Any?> = mutableMapOf(),
        type: PostType = PostType.JSON
    ): HttpResult {
        // 判断POST类型
        val req = when (type) {
            // FORM类型
            PostType.FORM -> {
                val form = FormBody.Builder()
                params["timestamp"] = System.currentTimeMillis()
                params.forEach {
                    form.add(it.key, it.value as String)
                }
                Request.Builder()
                    .url(url)
                    .post(form.build())
                    .build()
            }
            // JSON类型
            PostType.JSON -> {
                val body = JSON.toJSONString(params)
                val mediaType = "application/json;charset=utf-8".toMediaType()
                val requestBody = body.toRequestBody(mediaType)
                Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()
            }
        }
        return call(client.newCall(req))
    }

    /**
     * 下载文件
     * @param url 链接
     * @param path 报错路径
     * @throws ZLibException 下载失败
     */
    @JvmStatic
    @Throws(ZMusicException::class)
    fun download(url: String, path: String): HttpResult {
        val req = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(req)
        val res = call.execute()
        if (res.isSuccessful) {
            val result = HttpResult(res.code, res.body?.byteStream()?.saveData(path))
            res.close()
            return result
        } else {
            throw ZMusicException("下载失败! ${res.code} $url")
        }
    }


    private fun call(call: Call): HttpResult {
        return try {
            val res = call.execute()
            val result = HttpResult(res.code, res.body?.string())
            res.close()
            result
        } catch (e: Exception) {
            e.printStackTrace()
            HttpResult(500, "{\"msg\": \"${e.message}\"}")
        }
    }
}

/**
 * POST请求类型
 * @author 真心
 * @since 2021/4/14 20:44
 * @email qgzhenxin@qq.com
 */
enum class PostType {
    JSON,
    FORM
}
