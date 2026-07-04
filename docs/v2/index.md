---
title: V2 使用文档
---

# v2 使用文档 {#v2-docs}

欢迎查看 ZMusic 帮助文档，这里有所有您需要的帮助，如果您需要排查无法播放声音等问题，请点 [此处](/v2/faq)。

## 简介 {#introduction}

这是一个功能强大的音乐系统，支持以下功能。

- 全服点歌
- 单独播放
- 歌词显示
- 歌词翻译显示
- 多搜索源（网易云/哔哩哔哩）
- 关键词搜索
- 个人歌单
- 全服歌单
- 歌单播放（网易云）
- 音量调节（1.12 及以上支持）
- 支持 BungeeCord

## 下载 {#download}

### GitHub {#download-github}

<DownloadTable />

### 其他渠道 {#download-other}

<ExternalLinks />

## 下载 Mod {#download-mod}

### GitHub {#download-mod-github}

<ModDownload />

### 其他渠道 {#download-mod-other}

<ModExternalLinks />

## BungeeCord 说明 {#bungeecord}

- 只有 BungeeCord 端需要安装 ZMusic-Plugin 插件（子服不需要安装）
- BungeeCord 目前仅支持 1.9-1.19 的服务端
- BungeeCord 暂不支持经济系统
- 对于子服显示 Papi 变量、进度提示等功能，只需要对子服安装 ZMusic-Addon 插件

## 视频 {#video}

[点击前往哔哩哔哩查看演示视频](https://www.bilibili.com/video/av92156922)

## 反馈 {#feedback}

- 前往 [GitHub](https://github.com/starhui-dev/zmusic-plugin) 提交 Issues
- 加入交流群：[1032722724](https://jq.qq.com/?_wv=1027&k=5oIs7cc) 反馈

# 命令 {#commands}

## 主命令 {#main-commands}

- `/zm` 主命令
- `/zm help` 查看帮助
- `/zm play` 播放音乐
- `/zm music` 全服点歌
- `/zm search` 搜索音乐
- `/zm playlist` 歌单系统
- `/zm stop` 停止播放
- `/zm login` 登录网易云音乐

## 播放 {#play}

通过歌名搜索一个音乐，直接播放

### 命令 {#command}

`/zm play [搜索源] [歌名]`

[搜索源说明](#search-source)

### 示例 {#example}

`/zm play netease 你的猫咪`

## 点歌 {#music}

通过歌名搜索一个音乐，全服发送后，玩家点击播放

### 命令 {#command-1}

`/zm music [搜索源] [歌名]`

[搜索源说明](#search-source) [歌名 ID 化说明](#song-id)

### 示例 {#example-1}

`/zm music netease 你的猫咪`

## 搜索 {#search}

通过歌名搜索一个音乐，返回十首音乐的列表

### 命令 {#command-2}

`/zm search [搜索源] [歌名]`

[搜索源说明](#search-source) [歌名 ID 化说明](#song-id)

### 示例 {#example-2}

`/zm search netease 你的猫咪`

## 歌单 {#playlist}

通过导入歌单存储在服务器，方便播放歌单。

### 命令 {#command-3}

`/zm playlist [平台] [子命令]`

目前支持以下平台

- netease/163 - 网易云音乐

如果平台为 type 则为设置歌单播放方式。目前支持

- normal - 顺序播放
- loop - 循环播放
- random - 随机播放

示例：
`/zm playlist type random`

如果平台为 global 则为全局歌单模式。

- 子命令与普通模式相同

示例：
`/zm playlist global netease list`

`子命令` 对应平台的子命令

- `import` 通过歌单链接导入歌单
- 参数：`歌单链接` 对应平台的歌单链接
- `list` 检索指定平台的歌单列表
- `play` 通过歌单 ID 播放歌单（可用 list 获取）
- 参数：`歌单ID` 指定平台的歌单 ID

### 示例 {#example-3}

导入：

- `/zm playlist 163 import https://music.163.com/#/playlist?id=363046232`

播放：

- `/zm playlist 163 play 363046232`

## 管理员 {#admin}

管理员相关操作，全服强制播放，重载配置等

### 命令 {#command-4}

`/zm playall [搜索源] [歌名]` 强制全服播放
`/zm stopAll` 强制停止全服播放
`/zm reload` 重载配置文件

[搜索源说明](#search-source) [歌名 ID 化说明](#song-id)

### 示例 {#example-4}

`/zm playAll netease 你的猫咪`

# 权限 {#permissions}

## 普通玩家权限 {#player-permissions}

`zmusic.use` 可使用 play、stop 等普通指令

## 管理员权限 {#admin-permissions}

`zmusic.admin` 可使用 playAll、stopAll 等管理员指令

# 配置文件 {#configuration}

```json
{
  // 配置文件版本(请勿修改)
  "version": 11,
  // 是否字段检查更新
  "check-update": true,
  // 插件提示信息显示前缀
  "prefix": "&bZMusic &e>>> &r",
  // 是否开启调试模式
  "debug": false,
  // API设置
  "api": {
    // 网易云音乐API地址
    //
    // 使用开源项目NeteaseCloudMusicApi
    // 推荐自行部署，需Node.js环境
    // 地址: https://github.com/Binaryify/NeteaseCloudMusicApi
    "netease": "https://ncm.zhenxin.me/"
  },
  // 是否关注作者的网易云音乐账号
  "netease-follow": true,
  // ZMusic VIP 设置
  "vip": {
    // 授权账号
    "account": "1307993674",
    "secret": "none"
  },
  // 点歌设置
  "music": {
    // 点歌扣除的金币(设置为0则不扣除)
    // 拥有zmusic.bypass的玩家无视扣除
    "money": 10,
    // 点歌的冷却时间(设置为0则无冷却)
    // 拥有zmusic.bypass的玩家无视冷却
    "cooldown": 5
  },
  // 歌词设置
  "lyric": {
    // 是否启用歌词
    "enable": true,
    // 是否显示歌词翻译
    "showLyricTr": true,
    // 歌词颜色
    "color": "&b",
    // 以下为显示方式设置，可同时启用
    // 是否使用BossBar显示歌词(不支持1.8及以下)
    "bossBar": true,
    // 是否使用ActionBar显示歌词
    "actionBar": false,
    // 是否使用Title显示歌词
    "subTitle": false,
    // 是否使用聊天信息显示歌词
    "chatMessage": false,
    // Hud 设置(仅支持1.12及以上)
    "hud": {
      // 是否启用Hud
      "enable": true,
      // 信息的X坐标
      "infoX": 2,
      // 信息的Y坐标
      "infoY": 12,
      // 歌词的X坐标
      "lyricX": 2,
      // 歌词的Y坐标
      "lyricY": 72
    }
  }
}
```

# 变量 {#variables}

- `%zmusic_playing_name%` 获取当前播放的音乐歌名
- `%zmusic_playing_singer%` 获取当前播放的音乐歌手
- `%zmusic_playing_lyric%` 获取当前时间显示的歌词
- `%zmusic_time_current%` 获取当前播放的音乐的时间
- `%zmusic_time_max%` 获取当前播放的音乐的最大时间
- `%zmusic_playing_platform%` 获取当前播放的音乐平台
- `%zmusic_playing_source%` 获取当前播放的音乐来源

# 前置插件 {#dependencies}

## 全版本使用 {#all-versions}

- [`PlaceholderAPI`](https://www.spigotmc.org/resources/placeholderapi.6245/) [可选] 如需使用上方变量，请安装
- [`Vault`](https://www.spigotmc.org/resources/vault.34315/) [可选] 如果需要使用点歌扣费，请安装

## 1.5,1.6 版本使用 {#version-1-5-1-6}

- ~~[`AudioBuffer`](https://www.mcbbs.net/thread-832205-1-1.html) [必须] 用于播放音乐，贴内有配套 Mod 客户端需安装~~

## 1.4 及以下版本使用 {#version-1-4-below}

- ~~[`AudioBuffer`](https://www.mcbbs.net/thread-832205-1-1.html) [必须] 用于播放音乐，贴内有配套 Mod 客户端需安装~~
- ~~[`BossBarAPI`](https://www.mcbbs.net/thread-729531-1-1.html) [可选] 如需使用 BossBar 显示歌词，请安装~~
- ~~[`ActionBarAPI`](https://www.spigotmc.org/resources/actionbarapi-1-8-1-14-2.1315/) [可选] 如需使用 ActionBar 显示歌词，请安装~~

# 搜索源说明 {#search-source}

`搜索源` 为你要搜索音乐的平台，目前支持以下平台：

- netease/163 - 网易云音乐
- kuwo - 酷我音乐
- bilibili - 哔哩哔哩音乐

**QQ 音乐 API 已经完全移除，酷狗音乐播放时会出现问题**

# 歌名 ID 化说明 {#song-id}

- 将歌名替换为 `-id:音乐ID` 即可
- 目前支持网易云、哔哩哔哩音乐
- 示例：`/zm play bilibili -id:374305`
