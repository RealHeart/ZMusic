package me.zhenxin.zmusic.api

import me.zhenxin.zmusic.ZMusic

class ZMusicAPI {
    fun getVersion(): String = ZMusic.thisVer.toString()
}
