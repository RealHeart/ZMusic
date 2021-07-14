package me.zhenxin.zmusic.common.modules.api

/**
 * 音乐统一接口
 *
 * @author 真心
 * @since 2021/7/14 21:19
 * @email qgzhenxin@qq.com
 */
interface MusicApi {

    /**
     * 根据关键词搜索音乐
     *
     * @param keyword 关键词
     */
    fun searchSingle(keyword: String)

    /**
     * 分页搜索
     *
     * @param keyword 关键词
     * @param page 页数
     * @param count 每页返回数量
     */
    fun searchPage(keyword: String, page: Int, count: Int)

    /**
     * 获取歌单
     *
     * @param id 歌单ID
     */
    fun getPlaylist(id: String)

    /**
     * 获取专辑
     *
     * @param id 专辑ID
     */
    fun getAlbum(id: String)
}