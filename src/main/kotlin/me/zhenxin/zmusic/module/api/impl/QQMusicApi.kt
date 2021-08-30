package me.zhenxin.zmusic.module.api.impl

import me.zhenxin.zmusic.module.Config
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.data.MusicInfo

/**
 * QQ音乐接口实现
 *
 * @author 真心
 * @since 2021/8/30 13:26
 * @email qgzhenxin@qq.com
 */
class QQMusicApi : MusicApi {
    private val api = Config.API_QQ_LINK
    override val name: String = Lang.PLATFORM_QQ
    override fun searchSingle(keyword: String): MusicInfo {
        TODO("Not yet implemented")
    }

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        TODO("Not yet implemented")
    }

    override fun getPlaylist(id: String) {
        TODO("Not yet implemented")
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String {
        TODO("Not yet implemented")
    }
}