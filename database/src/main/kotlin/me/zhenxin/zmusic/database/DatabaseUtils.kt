package me.zhenxin.zmusic.database

import org.ktorm.database.Database

/**
 * 数据库工具类
 *
 * @author 真心
 * @since 2021/7/6 14:22
 * @email qgzhenxin@qq.com
 */
object DatabaseUtils {

    fun connect(hostname: String, port: Short, dbname: String, username: String, password: String) {
        database =
            Database.connect("jdbc:mysql://${hostname}:${port}/${dbname}", user = username, password = password)
    }
}

lateinit var database: Database
