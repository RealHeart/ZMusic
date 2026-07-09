package me.zhenxin.zmusic.platform.entity

import java.util.UUID

/**
 * 玩家发送者。
 *
 * @author 真心
 * @since 5.0.0
 */
interface ZPlayer : ZCommandSender {
    /** 玩家 UUID。 */
    val uniqueId: UUID
    /** 玩家对象固定为 true。 */
    override val isPlayer: Boolean get() = true
}
