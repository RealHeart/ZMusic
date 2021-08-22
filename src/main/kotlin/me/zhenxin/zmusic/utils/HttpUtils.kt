package me.zhenxin.zmusic.utils

import cn.hutool.http.HttpRequest
import cn.hutool.http.HttpUtil


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

    /**
     * GET获取
     * @param url 链接
     * @param paramsMap 参数(Map)
     * @return 字符串
     */
    @JvmStatic
    fun get(url: String, paramsMap: MutableMap<String, Any?> = mutableMapOf()): HttpResult {
        val params = HttpUtil.toParams(paramsMap)
        val req = HttpRequest.get("$url$params")
        val res = req.execute()
        return HttpResult(
            res.status,
            res.body()
        )
    }

    /**
     * POST获取
     * @param url 连接
     * @param paramsMap 参数
     */
    @JvmStatic
    fun post(url: String, paramsMap: MutableMap<String, Any?> = mutableMapOf()): HttpResult {
        val req = HttpRequest.post(url)
            .form(paramsMap)
        val res = req.execute()
        return HttpResult(
            res.status,
            res.body()
        )
    }
}