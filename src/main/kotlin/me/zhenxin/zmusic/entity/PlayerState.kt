package me.zhenxin.zmusic.entity

import taboolib.common.platform.ProxyPlayer

/**
 * 玩家状态
 *
 * @author 真心
 * @since 2021/11/8 13:21
 * @email qgzhenxin@qq.com
 */

private val stateMap = mutableMapOf<ProxyPlayer, StateInfo>()

/**
 * 获取玩家状态
 */
fun ProxyPlayer.getState(): StateInfo? {
    return stateMap[this]
}

/**
 * 设置成玩家状态
 *
 * @param stateInfo 状态信息
 */
fun ProxyPlayer.setState(stateInfo: StateInfo) {
    stateMap[this] = stateInfo
}