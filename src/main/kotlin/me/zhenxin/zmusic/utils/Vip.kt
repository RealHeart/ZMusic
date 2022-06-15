package me.zhenxin.zmusic.utils

import com.alibaba.fastjson2.JSON
import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.logger
import java.net.URLEncoder

/**
 * ZMusic VIP
 *
 * @author 真心
 * @since 2022/1/19 11:40
 */

/**
 * m4s转mp3
 */
fun m4s2mp3(id: String, url: String): String {
    val result = get(
        "https://api.zplu.cc/zmusic/vip/m4s2mp3?id=$id&url=${
            URLEncoder.encode(
                url,
                "UTF-8"
            )
        }&qq=${config.VIP_QQ}&key=${config.VIP_KEY}"
    )
    logger.debug(result)
    val json = JSON.parseObject(result)
    if (json.getIntValue("code") != 200) {
        return ""
    }
    return json.getString("data")
}