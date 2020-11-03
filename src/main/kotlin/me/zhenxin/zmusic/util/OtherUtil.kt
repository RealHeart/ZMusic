package me.zhenxin.zmusic.util

object OtherUtil {

    fun timeToSec(min: Int, sec: Int, mill: Int): Int = min * 60 * 1000 + sec * 1000 + mill

}
