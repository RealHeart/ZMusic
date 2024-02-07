package me.zhenxin.zmusic.config

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.toml.TomlParser
import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.logger
import java.util.*

/**
 * 国际化
 *
 * @author 真心
 * @since 2024/2/7 9:19
 */
object I18n {
    object Platform {
        val netease: String
            get() = i18n.get("platform.netease") ?: ""

        val bilibili: String
            get() = i18n.get("platform.bilibili") ?: ""
    }

    object Init {
        val loaded: List<String>
            get() = i18n.get("init.loaded") ?: emptyList()
    }

    object Update {
        val checking: String
            get() = i18n.get("update.checking") ?: ""

        val available: List<String>
            get() = i18n.get("update.available") ?: emptyList()

        val notAvailable: String
            get() = i18n.get("update.not_available") ?: ""
    }

    object Help {
        val tips: String
            get() = i18n.get("help.tips") ?: ""
    }
}

/**
 * 初始化国际化配置
 */
fun initI18n() {
    // 支持的语言
    val languages = arrayOf("en-US", "zh-CN")
    logger.debug("Supported languages: ${languages.joinToString()}")

    // 获取配置文件中的语言
    var language = Config.language

    // 如果是自动则使用系统语言
    if (language == "auto") {
        val locale = Locale.getDefault()
        language = "${locale.language}-${locale.country}"
    }

    // 如果不支持则使用默认语言
    if (language !in languages) {
        logger.warn("Language $language is not supported, use default language en-US.")
        logger.warn("Supported languages: ${languages.joinToString()}")
        language = "en-US"
    }

    // 从资源文件加载
    val classLoader = ZMusic::class.java.classLoader
    val file = classLoader.getResource("i18n/$language.toml")
    logger.debug("I18n file: $file")

    // 如果文件存在则加载, 否则禁用插件
    if (file != null) {
        val parser = TomlParser()
        i18n = parser.parse(file)
        logger.info("I18n is initialized.")
    } else {
        logger.error("Failed to load $language.toml, please check your plugin jar.")
        // TODO: disablePlugin()
    }
}

private lateinit var i18n: CommentedConfig