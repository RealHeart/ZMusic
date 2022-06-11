package me.zhenxin.zmusic.taboolib.extend.component

import taboolib.common.platform.ProxyCommandSender

interface Component {
    fun sendMsg(sender: ProxyCommandSender, component: net.kyori.adventure.text.Component)
}