package me.zhenxin.zmusic.module.taboolib.jsonmessage

import taboolib.common.platform.ProxyPlayer

/**
 * JSON消息多平台实现
 *
 * @author 真心
 * @since 2021/7/3 14:20
 * @email qgzhenxin@qq.com
 */
interface JsonMessage {
    /**
     * 发送点击消息
     * @param sender 玩家
     * @param base 基本消息
     * @param commands 命令
     */
    fun sendClickMessage(sender: ProxyPlayer, base: String, commands: Array<ClickCommand>)

    /**
     * 发送分页条
     * @param sender 玩家
     * @param pageBar 分页条消息内容 使用{prev} {next} 指定分页点击消息位置
     * @param prev 上一页命令
     * @param next 下一页命令
     */
    fun sendClickPageBar(sender: ProxyPlayer, pageBar: String, prev: ClickCommand, next: ClickCommand)
}