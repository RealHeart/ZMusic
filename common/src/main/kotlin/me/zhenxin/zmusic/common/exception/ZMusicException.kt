package me.zhenxin.zmusic.common.exception

/**
 * 自定义异常
 *
 * @author 真心
 * @since 2021/7/15 12:47
 * @email qgzhenxin@qq.com
 */
class ZMusicException(
    override val message: String = "未知错误"
) : RuntimeException()