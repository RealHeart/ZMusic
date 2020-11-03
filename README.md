# ZMusic

![][java] ![][kotlin] [![][release]](../../releases) ![][stars] [![][build-status]][build-link]

## 简介

这是一个功能强大的音乐系统，支持以下功能。
* 全服点歌
* 单独播放
* 歌词显示
* 歌词翻译显示(目前仅支持QQ音乐)
* 多搜索源(QQ/网易云/酷狗/酷我)
* 关键词搜索
* 个人歌单
* 全服歌单
* 歌单播放(QQ/网易云)
* 音量调节(AllMusic支持)

## 视频

[点击前往哔哩哔哩查看演示视频](https://www.bilibili.com/video/av92156922)

## 反馈

* 提交 [Issues](../../issues)
* 加入交流群：[1032722724](https://jq.qq.com/?_wv=1027&k=5oIs7cc) 反馈

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

## 本地化

你可以通过翻译语言文件来达到插件本地化的意图

#### 操作步骤：
1. Fork本仓库
2. 本地修改/添加语言文件
3. 提交Pull Request

语言文件修改/添加请参考 [示例文件](ZMusic-Plugin/src/main/resources/language/zh_CN.json)


[java]: https://badgen.net/badge/Java/1.8/green
[kotlin]: https://badgen.net/badge/Kotlin/1.4.10/green
[release]: https://badgen.net/github/release/RealHeart/ZMusic
[stars]: https://badgen.net/github/stars/RealHeart/ZMusic
[build-status]: https://badgen.net/jenkins/last-build/ci.zhenxin.xyz/job/Minecraft/job/ZMusic/
[build-link]: https://ci.zhenxin.xyz/job/Minecraft/job/ZMusic/
