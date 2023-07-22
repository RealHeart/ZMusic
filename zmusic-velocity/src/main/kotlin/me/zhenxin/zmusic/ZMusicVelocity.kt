package me.zhenxin.zmusic

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger

/**
 *
 *
 * @author 真心
 * @since 2022/7/14 13:24
 * @email qgzhenxin@qq.com
 */
@Plugin(id = "zmusic")
class ZMusicVelocity @Inject constructor(private var server: ProxyServer, private var logger: Logger) {

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        logger.info("ZMusicVelocity is loaded.")
    }
}