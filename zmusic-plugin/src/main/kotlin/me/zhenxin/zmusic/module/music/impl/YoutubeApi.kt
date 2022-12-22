package me.zhenxin.zmusic.module.music.impl

import cn.hutool.json.JSONArray
import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.entity.MusicInfo
import me.zhenxin.zmusic.entity.PlaylistInfo
import me.zhenxin.zmusic.module.music.MusicApi
import me.zhenxin.zmusic.utils.PostType
import me.zhenxin.zmusic.utils.post

/**
 *
 *
 * @author 舞晗坤
 * @since 2022/6/23 19:17
 * @email whksoft@gmail.com
 */
class YoutubeApi : MusicApi {
    //暂时硬编码
    override val name: String = "YouTube"
    private val api = "https://www.youtube.com"
    private val apiKey = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8"
    private val dlApi = "https://154.82.111.42.sslip.io"

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        val music = mutableListOf<MusicInfo>()
        val query = buildJsonQuery(keyword)
        val result = post("${api}/youtubei/v1/search?key=${apiKey}", query)
        val data = JSONObject(result)
        val songs = data.getJSONObject("contents")
                .getJSONObject("twoColumnSearchResultsRenderer")
                .getJSONObject("primaryContents")
                .getJSONObject("sectionListRenderer")
                .getJSONArray("contents")
                .getJSONObject(0)
                .getJSONObject("itemSectionRenderer")
                .getJSONArray("contents")

        songs.forEach {
            it as JSONObject
            val video = try {
                it.getJSONObject("videoRenderer")
            } catch (_: Exception) {
                null
            }

            if (video != null) {
                val videoId = video.getStr("videoId")
                val title = video.getJSONObject("title")
                        .getJSONArray("runs")
                        .getJSONObject(0)
                        .getStr("text")
                val author = video.getJSONObject("longBylineText")
                        .getJSONArray("runs")
                        .getJSONObject(0)
                        .getStr("text")
                val duration = video.getJSONObject("lengthText")
                        .getStr("simpleText")

                music.add(
                        MusicInfo(
                                videoId,
                                title,
                                author,
                                "",
                                "",
                                getDuration(duration)
                        )
                )
            }
        }
        return music
    }

    private fun buildJsonQuery(keyword: String): Map<String, Any> {
        val client = mapOf(
                "client" to mapOf(
                        "clientName" to "WEB",
                        "clientVersion" to "2.20220622.01.00"
                )
        )
        return mapOf(
                "context" to client,
                "query" to keyword
        )
    }

    private fun getDuration(duration: String): Int {
        val array = duration.split(":")
        return when (array.size) {
            1 -> array[0].toInt()
            2 -> array[0].toInt() * 60 + array[1].toInt()
            3 -> array[0].toInt() * 60 * 60 + array[1].toInt() * 60 + array[2].toInt()
            else -> 0
        }
    }

    override fun getPlaylist(id: String): PlaylistInfo {
        //获取playlist需要两个数据，一个是playlist的id，一个是其中的一个视频的id
        //目前考虑传入参数格式为v=dQw4w9WgXcQ&list=RDdQw4w9WgXcQ
        val params = id.split("&")
        if (params.size != 2) {
            throw IllegalArgumentException("Invalid playlist id format")
        }
        if (params[0].startsWith("v=")) {
            val videoId = params[0].substring(2)
            if (params[1].startsWith("list=")) {
                val playlistId = params[1].substring(5)
                return getPlaylist(videoId, playlistId)
            } else {
                throw IllegalArgumentException("Invalid playlist id format")
            }
        } else if (params[0].startsWith("list=")) {
            val playlistId = params[0].substring(5)
            if (params[1].startsWith("v=")) {
                val videoId = params[1].substring(2)
                return getPlaylist(videoId, playlistId)
            } else {
                throw IllegalArgumentException("Invalid playlist id format")
            }
        } else {
            throw IllegalArgumentException("Invalid playlist id format")
        }
    }

    private fun getPlaylist(videoId: String, playListId: String): PlaylistInfo {
        TODO("not yet implemented")
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String {
        val data = post(
                "$dlApi/newp", mapOf(
                "u" to "https://www.youtube.com/watch?v=$id",
                "c" to "HK"
        ), type = PostType.FORM
        )
        val json = JSONObject(data)
        val mp3 = json.getJSONObject("data").getJSONArray("mp3")
        if (mp3.isNullOrEmpty()) {
            val mp3Cdn: JSONArray = json.getJSONObject("data").getJSONArray("mp3_cdn")
            //遍历
            for (m in mp3Cdn) {
                //寻找mp3格式的对象
                val mp3InCdn = m as JSONObject
                if ("mp3" == mp3InCdn.getStr("mp3_format")) {
                    return mp3InCdn.getStr("mp3_url")
                }
            }
            //如果没有找到mp3格式的对象，就返回第一个
            return mp3Cdn.getJSONObject(0).getStr("mp3_url")
        } else {
            //遍历
            for (m in mp3) {
                //寻找mp3格式的对象
                val mp3InArray = m as JSONObject
                if ("mp3" == mp3InArray.getStr("mp3_format")) {
                    //mod端暂时有点问题
                    return dlApi.replace("https://", "http://") + mp3InArray.getStr("mp3_url")
                }
            }
            return dlApi.replace("https://", "http://") + mp3.getJSONObject(0).getStr("mp3_url")
        }
    }

    override fun getLyric(id: String): MutableList<LyricRaw> {
        return mutableListOf(LyricRaw(1, "", ""))
    }

    override fun getMusicInfo(id: String): MusicInfo {
        val query = buildJsonQuery("KrNUrgaOsCc")
        val result = post("${api}/youtubei/v1/player?key=$apiKey", query)
        val data = JSONObject(result)
        val videoDetails = data.getJSONObject("videoDetails")
        val title = videoDetails.getStr("title")
        val lengthSeconds = videoDetails.getStr("lengthSeconds")
        return MusicInfo(
                id,
                title,
                "",
                "",
                "",
                lengthSeconds.toInt()
        )
    }
}