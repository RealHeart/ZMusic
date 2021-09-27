package me.zhenxin.zmusic.type

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
    val supportAccount: Boolean
) {
    NETEASE(true, true),
    QQ(true, false),
    BILIBILI(false, false),
    KUGOU(false, false),
    XIMA(false, false),
    SOUNDCLOUD(false, false)
}

fun getPlatformNames(): List<String> {
    return listOf(
        MusicPlatform.NETEASE.name.lowercase(),
        MusicPlatform.QQ.name.lowercase(),
        MusicPlatform.BILIBILI.name.lowercase(),
        MusicPlatform.KUGOU.name.lowercase(),
        MusicPlatform.XIMA.name.lowercase(),
        MusicPlatform.SOUNDCLOUD.name.lowercase()
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