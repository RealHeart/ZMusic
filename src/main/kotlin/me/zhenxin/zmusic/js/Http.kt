package me.zhenxin.zmusic.js

/**
 * JavaScript HTTP
 *
 * @author 真心
 * @since 2022/6/15 1:27
 */
class Http {

    fun get(url: String) = me.zhenxin.zmusic.utils.get(url)


    fun post(url: String, data: MutableMap<String, Any?>) = me.zhenxin.zmusic.utils.post(url, data)
}