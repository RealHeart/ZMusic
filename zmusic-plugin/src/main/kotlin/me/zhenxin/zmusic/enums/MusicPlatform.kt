package me.zhenxin.zmusic.enums

/**
 * 音乐平台
 *
 * @author 真心
 * @since 2021/9/27 20:51
 * @email qgzhenxin@qq.com
 */
@Suppress("SpellCheckingInspection")
enum class MusicPlatform(
    val supportPlaylist: Boolean,
    val supportAccount: Boolean,
    val supportIdPlay: Boolean
) {
    NETEASE(true, true, true),
    BILIBILI(false, false, true),
    XIMA(false, false, false),
    SOUNDCLOUD(false, false, false),
    YOUTUBE(false, false, false),
}

fun getPlatformNames(): List<String> {
    return listOf(
        MusicPlatform.NETEASE.name.lowercase(),
        MusicPlatform.BILIBILI.name.lowercase(),
        MusicPlatform.XIMA.name.lowercase(),
        MusicPlatform.SOUNDCLOUD.name.lowercase(),
        MusicPlatform.YOUTUBE.name.lowercase()
    )
}

fun getPlatformNamesWithSupportPlaylist(): List<String> {
    return getPlatformNames().map {
        val platform = MusicPlatform.valueOf(it.uppercase())
        val list = mutableListOf<String>()
        if (platform.supportPlaylist) {
            list.add(it)
        }
        return list
    }
}

fun getPlatformNamesWithSupportAccount(): List<String> {
    return getPlatformNames().map {
        val platform = MusicPlatform.valueOf(it.uppercase())
        val list = mutableListOf<String>()
        if (platform.supportAccount) {
            list.add(it)
        }
        return list
    }
}

fun getPlatformNamesWithSupportIdPlay(): List<String> {
    return getPlatformNames().map {
        val platform = MusicPlatform.valueOf(it.uppercase())
        val list = mutableListOf<String>()
        if (platform.supportIdPlay) {
            list.add(it)
        }
        return list
    }
}

fun String.asMusicPlatform(): MusicPlatform {
    return MusicPlatform.valueOf(uppercase())
}