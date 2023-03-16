package me.zhenxin.zmusic.module.music.impl

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.entity.MusicInfo
import me.zhenxin.zmusic.entity.PlaylistInfo
import me.zhenxin.zmusic.module.music.MusicApi
import me.zhenxin.zmusic.utils.get

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
    private val api = "https://pipedapi.syncpundit.io"

    override fun searchPage(keyword: String, page: Int, count: Int): List<MusicInfo>? {
        val result = get("$api/search?q=$keyword&filter=all")
        val data = JSONObject(result)
        val songs = data.getJSONArray("items")
        if (songs.isNullOrEmpty()) {
            return null
        }

        val videoArray = songs.stream().map { video ->
            video as JSONObject
            MusicInfo(
                video.getStr("url").replaceFirst("/watch?v=", ""),
                video.getStr("title"),
                video.getStr("uploaderName"),
                "",
                "",
                video.getInt("duration")
            )
        }.toList()
        if (videoArray.isEmpty()) {
            return null
        }
        return videoArray
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

    override fun getPlayUrl(id: String): String? {
        val data = get(
            "$api/streams/$id"
        )
        val json = JSONObject(data)
        val audioStreams = json.getJSONArray("audioStreams")
        if (audioStreams.isNullOrEmpty()) {
            return null
        }

        val opusAudio = audioStreams.stream().filter { audio ->
            val format = (audio as JSONObject).getStr("format")
            format != null && format.equals("WEBMA_OPUS")
        }.max { audio, audio2 ->
            (audio as JSONObject).getInt("bitrate").compareTo((audio2 as JSONObject).getInt("bitrate"))
        }.orElse(null)
        if (opusAudio != null) {
            return (opusAudio as JSONObject).getStr("url")
        }

        val m4aAudio = audioStreams.stream().filter { audio ->
            val format = (audio as JSONObject).getStr("format")
            format != null && format.equals("M4A")
        }.max { audio, audio2 ->
            (audio as JSONObject).getInt("bitrate").compareTo((audio2 as JSONObject).getInt("bitrate"))
        }.orElse(null)
        if (m4aAudio != null) {
            return (m4aAudio as JSONObject).getStr("url")
        }

        return null
    }

    override fun getLyric(id: String): MutableList<LyricRaw>? {
        return null
    }

    override fun getMusicInfo(id: String): MusicInfo {
        val data = get(
            "$api/streams/$id"
        )
        val json = JSONObject(data)
        return MusicInfo(
            id,
            json.getStr("title"),
            json.getStr("uploader"),
            "",
            "",
            json.getInt("duration")
        )
    }
}