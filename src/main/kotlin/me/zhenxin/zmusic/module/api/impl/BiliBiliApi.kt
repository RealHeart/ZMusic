package me.zhenxin.zmusic.module.api.impl

import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.MusicInfo
import me.zhenxin.zmusic.module.api.PlaylistInfo

/**
 * 哔哩哔哩音乐实现
 *
 * @author 真心
 * @since 2021/9/8 10:31
 * @email qgzhenxin@qq.com
 */
class BiliBiliApi : MusicApi {
    override val name: String = Lang.PLATFORM_BILI

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        TODO("Not yet implemented")
    }

    override fun getPlaylist(id: String): PlaylistInfo {
        TODO("Not yet implemented")
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String {
        TODO("Not yet implemented")
    }

}