package me.zhenxin.zmusic.music

/**
 * 音乐搜索接口。
 *
 * @author 真心
 * @since 5.0.0
 */
interface MusicCatalog {
    /**
     * 按关键词搜索音乐。
     *
     * @param keyword 搜索关键词
     * @param limit 最大结果数量
     * @return 搜索结果
     */
    fun search(keyword: String, limit: Int): List<Song>
}
