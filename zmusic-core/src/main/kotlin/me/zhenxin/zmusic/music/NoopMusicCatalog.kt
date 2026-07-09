package me.zhenxin.zmusic.music

/**
 * 空音乐源。
 *
 * @author 真心
 * @since 5.0.0
 */
class NoopMusicCatalog : MusicCatalog {
    /**
     * 返回空搜索结果。
     *
     * @param keyword 搜索关键词
     * @param limit 最大结果数量
     * @return 空列表
     */
    override fun search(keyword: String, limit: Int): List<Song> {
        return emptyList()
    }
}
