package me.zhenxin.zmusic.exception

/**
 * ZMusic 异常
 *
 * @author 真心
 * @since 2023/7/24 10:07
 * @email qgzhenxin@qq.com
 */
class ZMusicException(
    override val message: String = "ZMusic Internal Exception"
) : RuntimeException()