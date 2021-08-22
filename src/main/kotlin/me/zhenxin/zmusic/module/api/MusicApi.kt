package me.zhenxin.zmusic.module.api

import me.zhenxin.zmusic.module.api.data.MusicInfo

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
    fun searchSingle(keyword: String): MusicInfo

    /**
     * 分页搜索
     *
     * @param keyword 关键词
     * @param page 页数
     * @param count 每页返回数量
     */
    fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo>

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


    /**
     * 获取音乐播放链接
     *
     * @param id 音乐ID
     */
    fun getPlayUrl(id: String): String
}