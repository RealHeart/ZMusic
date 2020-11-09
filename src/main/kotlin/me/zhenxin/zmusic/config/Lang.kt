package me.zhenxin.zmusic.config


object Lang {

    object Plugin {
        val author = "真心"
        val website = "www.zhenxin.xyz"
        val qq = "1307993674"
        val group = "1032722724"
    }

    object Loading {
        var loading = "正在加载中...."
        var configLoaded = "成功加载配置文件!"
        var pluginAuthor = "插件作者: "
        var blog = "博客: "
        val qq = "QQ: "
        var group = "插件交流群: "
        var loaded = "插件已加载完成!"
    }

    object Help {
        var tip = "输入 /zm help 查看帮助."
        var main = arrayOf(
            "/zm help - 查看主帮助(当前).",
            "/zm help play - 查看播放帮助.",
            "/zm help music - 查看点歌帮助.",
            "/zm help search - 查看搜索帮助.",
            "/zm help playlist - 查看歌单帮助.",
            "[admin]/zm help admin - 查看管理员帮助.",
            "&6=========================================",
            "/zm stop - 停止播放",
            "/zm loop - 开/关循环播放",
            "/zm url [MP3地址] - 播放网络音乐",
            "/zm 163hot [歌名] - 获取网易云音乐热评(前三)",
            "&6========================================="
        )
    }

    object Play {
        var error: Array<String> = arrayOf(
            ""
        )
    }
}
