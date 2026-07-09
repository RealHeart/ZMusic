package me.zhenxin.zmusic.platform.entity

/**
 * 命令发送者。
 *
 * @author 真心
 * @since 5.0.0
 */
interface ZCommandSender {
    /** 发送者名称。 */
    val name: String
    /** 是否是玩家。 */
    val isPlayer: Boolean

    /**
     * 判断是否拥有权限。
     *
     * @param permission 权限节点
     * @return true 表示有权限
     */
    fun hasPermission(permission: String): Boolean

    /**
     * 发送文本消息。
     *
     * @param message 消息内容
     */
    fun sendMessage(message: String)

    /**
     * 转为玩家对象。
     *
     * @return 玩家对象；非玩家返回 null
     */
    fun asPlayer(): ZPlayer? = this as? ZPlayer
}
