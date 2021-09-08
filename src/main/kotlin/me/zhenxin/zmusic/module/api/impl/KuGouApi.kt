package me.zhenxin.zmusic.module.api.impl

import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.MusicInfo

/**
 * 酷狗音乐实现
 *·
 * @author 真心
 * @since 2021/9/8 10:28
 * @email qgzhenxin@qq.com
 */
class KuGouApi : MusicApi {
    override val name: String = Lang.PLATFORM_KUGOU

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