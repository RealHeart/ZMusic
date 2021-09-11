package me.zhenxin.zmusic.module.taboolib.component

import taboolib.common.platform.ProxyCommandSender

interface Component {
    fun sendMsg(sender: ProxyCommandSender, component: me.zhenxin.adventure.text.Component)
}