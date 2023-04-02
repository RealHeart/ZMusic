package me.zhenxin.zmusic.module.music.impl

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.config.Config
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
    override val name: String = "YouTube"
    private val api = Config.YOUTUBE_API_LINK

    override fun searchPage(keyword: String, page: Int, count: Int): List<MusicInfo>? {
        val data = JSONObject(get("$api/search?q=$keyword&filter=all"))
        val songs = data.getJSONArray("items")
        if (songs.isNullOrEmpty()) {
            return null
        }

        val videoArray = songs.map { video ->
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
        return videoArray
    }

    override fun getPlaylist(id: String): PlaylistInfo? {
        // untested
        val json = JSONObject(get("$api/playlists/$id"))
        val name = json.getStr("name")
        val streams = json.getJSONArray("relatedStreams")
        if (streams.isNullOrEmpty()) {
            return null
        }

        val videoArray = streams.map { stream ->
            stream as JSONObject
            getMusicInfo(stream.getStr("url").replaceFirst("/watch?v=", ""))
        }.toList()
        return PlaylistInfo(id, name, videoArray)
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String? {
        val json = JSONObject(get("$api/streams/$id"))
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