package me.zhenxin.zmusic.js

/**
 * .
 *
 * @author 真心
 * @since 2022/6/15 11:02
 */
class Util {
    fun mergeSingers(singers: MutableList<String>): String {
        var singer = ""
        singers.forEach {
            singer = "$singer$it/"
        }
        singer = singer.trimEnd('/')
        return singer
    }
}