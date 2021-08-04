package me.zhenxin.zmusic.common.modules.sender

/**
 * Sender 基本接口
 *
 * @author 真心
 * @since 2021/7/20 11:47
 * @email qgzhenxin@qq.com
 */
interface Sender {

    /**
     * 发送信息
     *
     * @param msg 信息
     */
    fun sendMsg(msg: String)

    /**
     * 检测是否拥有权限
     *
     * @param pem 权限
     */
    fun hasPem(pem: String): Boolean

    /**
     * 是否为玩家
     */
    val isPlayer: Boolean

    /**
     * 获取名称
     */
    val name: String

    /**
     * 获取玩家UUID
     */
    val playerUniqueId: String

}