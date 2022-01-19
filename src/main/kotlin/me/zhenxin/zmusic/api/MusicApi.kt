package me.zhenxin.zmusic.api

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject


/**
 * 音乐统一接口
 *
 * @author 真心
 * @since 2021/7/14 21:19
 * @email qgzhenxin@qq.com
 */
interface MusicApi {

    /**
     * 平台名称
     */
    val name: String

    /**
     * 根据关键词搜索音乐
     *
     * @param keyword 关键词
     */
    fun searchSingle(keyword: String): MusicInfo {
        if (keyword.contains("-id:")) {
            try {
                val id = keyword.split("-id:")[1]
                return getMusicInfo(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val data = searchPage(keyword, 1, 1)
        return data[0]
    }

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
    fun getPlaylist(id: String): PlaylistInfo

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

    fun mergeSingers(singers: JSONArray): String {
        var singer = ""
        singers.forEach { ar ->
            ar as JSONObject
            singer = "$singer${ar.getString("name")}/"
        }
        singer = singer.trimEnd('/')
        return singer
    }

    fun getMusicInfo(id: String): MusicInfo
}