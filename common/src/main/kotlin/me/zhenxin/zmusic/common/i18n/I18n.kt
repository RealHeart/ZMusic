package me.zhenxin.zmusic.common.i18n

import me.zhenxin.zmusic.common.config.Config
import me.zhenxin.zmusic.common.utils.HttpUtil
import org.yaml.snakeyaml.Yaml
import java.io.File

/**
 * 语言信息
 *
 * @author 真心
 * @since 2021/7/15 11:58
 * @email qgzhenxin@qq.com
 */
object I18n {
    var INIT_LOADING = ""

    fun load(dataFolder: File) {
        val dir = File(dataFolder, "/i18n")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val file = File(dataFolder, "/i18n/${Config.Language}.yml")
        if (!file.exists()) {
            HttpUtil.download(
                "https://cdn.jsdelivr.net/gh/RealHeart/ZMusic-i18n/${Config.Language}.yml",
                file.absolutePath
            )
        }
        val yaml = Yaml()
        val lang: LinkedHashMap<String, Any> = yaml.load(file.inputStream())

        println(lang)

        INIT_LOADING = (lang["init"] as LinkedHashMap<*, *>)["loading"] as String
    }
}