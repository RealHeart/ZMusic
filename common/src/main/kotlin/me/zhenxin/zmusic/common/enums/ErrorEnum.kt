package me.zhenxin.zmusic.common.enums

/**
 * 错误信息枚举类
 *
 * @author 真心
 * @since 2021/7/14 21:40
 * @email qgzhenxin@qq.com
 */
enum class ErrorEnum(val code: Short, val msg: String) {
    UNSUPPORTED(4009, "暂不支持此功能")
}