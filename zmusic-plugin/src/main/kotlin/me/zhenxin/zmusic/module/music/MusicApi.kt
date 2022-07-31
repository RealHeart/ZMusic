package me.zhenxin.zmusic.module.music

import cn.hutool.json.JSONArray
import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.entity.MusicInfo
import me.zhenxin.zmusic.entity.PlaylistInfo


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
     * @return 音乐信息
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
     * @return 音乐列表
     */
    fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo>

    /**
     * 获取歌单
     *
     * @param id 歌单ID
     * @return 歌单信息
     */
    fun getPlaylist(id: String): PlaylistInfo

    /**
     * 获取专辑
     *
     * @param id 专辑ID
     * @return 专辑信息
     */
    fun getAlbum(id: String)


    /**
     * 获取音乐播放链接
     *
     * @param id 音乐ID
     * @return 音乐播放链接
     */
    fun getPlayUrl(id: String): String

    /**
     * 获取歌词
     * @param id String 音乐ID
     * @return LyricRaw 歌词
     */
    fun getLyric(id: String): MutableList<LyricRaw>

    fun mergeSingers(singers: JSONArray): String {
        var singer = ""
        singers.forEach {
            it as JSONObject
            singer = "$singer${it.getStr("name")}/"
        }
        singer = singer.trimEnd('/')
        return singer
    }

    fun getMusicInfo(id: String): MusicInfo
}