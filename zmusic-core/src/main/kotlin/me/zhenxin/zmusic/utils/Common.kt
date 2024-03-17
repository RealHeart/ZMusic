package me.zhenxin.zmusic.utils

import me.zhenxin.zmusic.ZMusicConstants
import me.zhenxin.zmusic.config.I18n
import me.zhenxin.zmusic.entity.VersionInfo
import me.zhenxin.zmusic.logger
import org.dromara.hutool.json.JSONUtil
import kotlin.math.log

/**
 * 公共工具
 *
 * @author 真心
 * @since 2023/7/23 15:49
 * @email qgzhenxin@qq.com
 */

/**
 * 检查更新
 */
fun checkUpdate() {
    logger.info(I18n.Update.checking)
    val res = httpGet("https://api.zhenxin.me/zmusic/version?type=dev")
    val json = JSONUtil.parseObj(res)
    if (json.getInt("code") != 200) {
        logger.error(I18n.Update.checkFailed)
        return
    }

    val data = json.getJSONObject("data")
    val versionInfo = JSONUtil.toBean(data, VersionInfo::class.java)

    logger.debug("Current version: ${ZMusicConstants.PLUGIN_VERSION}(${ZMusicConstants.PLUGIN_VERSION_CODE})")
    logger.debug("Latest version: ${versionInfo.version}(${versionInfo.versionCode})")

    if (versionInfo.versionCode > ZMusicConstants.PLUGIN_VERSION_CODE.toLong()) {
        I18n.Update.available.forEach {
            if (it.contains("{version}")) {
                logger.info(it.replace("{version}", versionInfo.version))
            } else if (it.contains("{changelog}")) {
                versionInfo.changelog.split("\n").forEach { log ->
                    logger.info("&b$log")
                }
            } else if (it.contains("{download}")) {
                logger.info(it.replace("{download}", versionInfo.download))
            } else {
                logger.info(it)
            }
        }
    } else {
        logger.info(I18n.Update.notAvailable)
    }
}