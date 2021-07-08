# ZMusic

![][java] ![][kotlin] [![][release]](../../releases) [![][build-status]][build-link]

3.0版本正在开发中 2.0版本请查看[master](../../tree/master/)分支

## 简介

这是一个功能强大的音乐系统，支持以下功能。
* 全服点歌
* 单独播放
* 歌词显示
* 歌词翻译显示
* 多搜索源(QQ/网易云/酷狗/酷我/哔哩哔哩)
* 关键词搜索
* 个人歌单
* 全服歌单
* 歌单播放(QQ音乐/网易云)
* 音量调节(AllMusic支持)
* 支持BungeeCord

## 视频

[点击前往哔哩哔哩查看演示视频](https://www.bilibili.com/video/av92156922)

## 反馈

* 提交 [Issues](../../issues)
* 加入交流群：[1032722724](https://jq.qq.com/?_wv=1027&k=5oIs7cc) 反馈

## 模块

| 名称 | 说明 |
| --- | --- |
| common | 公共文件 |
| bukkit | Bukkit/Spigot/Paper 插件 |
| bungee | Bungeecord/Waterfall 插件 |
| merge | 全部平台合并插件 |

## 构建

1. 克隆

   > ```shell
   > git clone https://gitee.com/RealHeart/ZMusic # Gitee-码云
   > ```
   > ```shell
   > git clone https://github.com/RealHeart/ZMusic # Github
   > ```

2. 构建

   > ```shell
   > cd ZMusic
   > gradlew clean build
   > ```

## 使用

1. 在[release](../../releases)页或[CI构建][build-link]下载最新版本

2. 放入服务端插件目录

3. 运行服务器

## 文档

[点击查看使用文档](https://zmusic.zhenxin.xyz/)


[java]: https://badgen.net/badge/Java/16/green
[kotlin]: https://badgen.net/badge/Kotlin/1.5.20/green
[release]: https://badgen.net/github/release/RealHeart/ZMusic
[build-status]: https://badgen.net/jenkins/last-build/ci.zhenxin.xyz/job/Minecraft/job/ZMusic/
[build-link]: https://ci.zhenxin.xyz/job/Minecraft/job/ZMusic/
