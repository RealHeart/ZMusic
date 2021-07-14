package me.zhenxin.zmusic.common.modules.api.impl

import me.zhenxin.zmusic.common.modules.api.MusicApi

/**
 * 网易云音乐接口实现
 *
 * @author 真心
 * @since 2021/7/14 21:37
 * @email qgzhenxin@qq.com
 */
class NeteaseApi : MusicApi {
    override fun searchSingle(keyword: String) {
        TODO("Not yet implemented")
    }

    override fun searchPage(keyword: String, page: Int, count: Int) {
        TODO("Not yet implemented")
    }

    override fun getPlaylist(id: String) {
        TODO("Not yet implemented")
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }
}