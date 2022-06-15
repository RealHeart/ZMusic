package me.zhenxin.zmusic.js

import com.alibaba.fastjson2.JSONObject

/**
 * .
 *
 * @author 真心
 * @since 2022/6/15 11:02
 */
class Util {
    fun mergeSingers(singers: MutableList<String>): String {
        var singer = ""
        singers.forEach { ar ->
            ar as JSONObject
            singer = "$singer${ar.getString("name")}/"
        }
        singer = singer.trimEnd('/')
        return singer
    }
}