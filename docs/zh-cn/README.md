# ZMusic

![][java]
![][kotlin]
![][license]
![][release]
![][downloads]
![][players]
![][servers]
![][tested-versions]

欢迎查看 ZMusic 帮助文档，这里有所有您需要的帮助，如果您需要排查无法播放声音等问题，请点[此处](#faq)。

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
* 音量调节(1.12及以上支持)
* 支持BungeeCord

## 客户端Mod说明

本插件需要客户端安装配套Mod才能正常播放

* 对于1.8-1.11的客户端 请安装[`AudioBuffer`](https://www.mcbbs.net/thread-832205-1-1.html) Mod
* 对于1.7.10, 1.12.2-1.17.1的客户端 请安装[`AllMusic`](https://pan.baidu.com/s/1_QH-JbYT84vg3sbk5_xxxw) Mod （提取码为`ibiv`）

## BungeeCord说明

* BungeeCord 目前仅支持1.9-1.17的服务端
* BungeeCord 暂不支持经济系统
* 对于子服显示Papi变量 进度提示等功能 需要安装ZMusic-Addon插件

## 视频

[点击前往哔哩哔哩查看演示视频](https://www.bilibili.com/video/av92156922)

## 反馈

* 前往 [码云](https://gitee.com/realheart/ZMusic) 或 [Github](https://github.com/RealHeart/ZMusic) 提交 Issues
* 加入交流群：[1032722724](https://jq.qq.com/?_wv=1027&k=5oIs7cc) 反馈

# 命令

## 主命令

`/zm` 主命令  
`/zm help` 查看帮助  
`/zm play` 播放音乐  
`/zm music` 全服点歌  
`/zm search` 搜索音乐  
`/zm playlist` 歌单系统

## 播放

通过歌名搜索一个音乐，直接播放

### 命令

`/zm play [搜索源] [歌名]`

[搜索源说明](#搜索源说明)

### 示例

`/zm play qq 你的猫咪`

## 点歌

通过歌名搜索一个音乐，全服发送后，玩家点击播放

### 命令

`/zm music [搜索源] [歌名]`

[搜索源说明](#搜索源说明) [歌名ID化说明](#歌名ID化说明)

### 示例

`/zm music qq 你的猫咪`

## 搜索

通过歌名搜索一个音乐，返回十首音乐的列表

### 命令

`/zm search [搜索源] [歌名]`

[搜索源说明](#搜索源说明) [歌名ID化说明](#歌名ID化说明)

### 示例

`/zm search qq 你的猫咪`

## 歌单

通过导入歌单存储在服务器，方便播放歌单。

### 命令

`/zm playlist [平台] [子命令]`

目前支持以下平台

* qq - QQ音乐
* netease/163 - 网易云音乐

如果平台为type 则为设置歌单播放方式.  
目前支持

* normal - 顺序播放
* loop - 循环播放
* random - 随机播放

示例:
`/zm playlist type random`

如果平台为global 则为全局歌单模式.

* 子命令与普通模式相同

示例:
`/zm playlist global qq list`

`子命令` 对应平台的子命令

* `import` 通过歌单链接导入歌单
    * 参数
        * `歌单链接` 对应平台的歌单链接
* `list` 检索指定平台的歌单列表
* `play` 通过歌单ID播放歌单(可用list获取)
    * 参数
        * `歌单ID` 指定平台的歌单ID

### 示例

导入:

* `/zm playlist qq import https://y.qq.com/n/yqq/playlist/1937967578.html`
* `/zm playlist 163 import https://music.163.com/#/playlist?id=363046232`

播放:

* `/zm playlist qq play 1937967578`
* `/zm playlist 163 play 363046232`

## 管理员

管理员相关操作，全服强制播放，重载配置等

### 命令

`/zm palyall [搜索源] [歌名]` 强制全服播放
`/zm stopAll` 强制停止全服播放  
`/zm reload` 重载配置文件

[搜索源说明](#搜索源说明) [歌名ID化说明](#歌名ID化说明)

### 示例

`/zm playAll qq 你的猫咪`

# 权限

## 普通玩家权限

`zmusic.use` 可使用play,stop等普通指令`

## 管理员权限

`zmusic.admin` 可使用playAll,stopAll等管理员指令

# 配置文件

```json
{
  /// 配置文件版本(请勿修改)
  "version": 9,
  /// 是否自动下载插件更新
  "update": false,
  /// 插件提示信息显示前缀
  "prefix": "&bZMusic &e>>> &r",
  /// 是否开启调试模式
  "debug": false,
  /// API设置
  "api": {
    /// 网易云音乐API地址
    ///
    /// 使用开源项目NeteaseCloudMusicApi
    /// 推荐自行部署，需Node.js环境
    /// 地址: https://github.com/Binaryify/NeteaseCloudMusicApi
    "netease": "https://netease.api.zhenxin.xyz/",
    /// QQ音乐API地址
    ///
    /// 使用开源项目QQMusicApi
    /// 推荐自行部署，需Node.js环境
    /// 地址: https://github.com/jsososo/QQMusicApi
    "qq": "https://qqmusic.api.zhenxin.xyz/"
  },
  /// 账号设置
  "account": {
    /// 网易云音乐
    "netease": {
      /// 登录方式
      ///
      /// email - 邮箱登录(密码处填写邮箱密码)
      /// phone - 手机号登录(密码处填写网易云密码)
      "loginType": "phone",
      /// 账号
      ///
      /// 邮箱登录填写邮箱
      /// 手机号登录填写手机号
      "account": "18888888888",
      /// 密码
      ///
      /// 邮箱登录填写邮箱密码
      /// 手机号登录填写网易云密码
      "password": "a123456",
      /// 密码方式
      ///
      /// normal = 纯密码 由插件通过md5加密上传到服务器验证
      /// md5 = 纯md5 由用户通过md5加密设置在配置文件 插件读取上传到服务器验证
      "passwordType": "normal",
      /// 是否关注作者的网易云音乐账号
      "follow": true
    },
    /// 哔哩哔哩
    "bilibili": {
      /// 由于哔哩哔哩为m4a音频格式
      /// 需要服务器用来转换
      /// 因此哔哩哔哩播放功能收费5元/永久
      /// 联系作者获取授权
      ///
      /// 授权QQ
      "qq": "1307993674",
      /// 授权Key
      "key": "none"
    }
  },
  /// 点歌设置
  "music": {
    /// 点歌扣除的金币(设置为0则不扣除)
    /// 拥有zmusic.bypass的玩家无视扣除
    "money": 10,
    /// 点歌的冷却时间(设置为0则无冷却)
    /// 拥有zmusic.bypass的玩家无视冷却
    "cooldown": 5
  },
  /// 歌词设置
  "lyric": {
    /// 是否启用歌词
    "enable": true,
    /// 是否显示歌词翻译
    "showLyricTr": true,
    /// 歌词颜色
    "color": "&b",
    /// 以下为显示方式设置，可同时启用
    /// 是否使用BossBar显示歌词(不支持1.8及以下)
    "bossBar": true,
    /// 是否使用ActionBar显示歌词
    "actionBar": false,
    /// 是否使用Title显示歌词
    "subTitle": false,
    /// 是否使用聊天信息显示歌词
    "chatMessage": false,
    /// Hud 设置(仅支持1.12及以上)
    "hud": {
      /// 是否启用Hud
      "enable": true,
      /// 信息的X坐标
      "infoX": 2,
      /// 信息的Y坐标
      "infoY": 12,
      /// 歌词的X坐标
      "lyricX": 2,
      /// 歌词的Y坐标
      "lyricY": 72
    }
  }
}
```

# 变量

`%zmusic_playing_name%` 获取当前播放的音乐歌名  
`%zmusic_playing_singer%` 获取当前播放的音乐歌手  
`%zmusic_playing_lyric%` 获取当前时间显示的歌词  
`%zmusic_time_current%` 获取当前播放的音乐的时间  
`%zmusic_time_max%` 获取当前播放的音乐的最大时间  
`%zmusic_playing_platform%` - 获取当前播放的音乐平台  
`%zmusic_playing_source%` - 获取当前播放的音乐来源

# 前置插件

## 全版本使用

[```PlaceholderAPI```](https://www.spigotmc.org/resources/placeholderapi.6245/) [可选] 如需使用上方变量 请安装   
[```Vault```](https://www.spigotmc.org/resources/vault.34315/) [可选] 如果需要使用点歌扣费 请安装

## 1.5,1.6版本使用

~~[```AudioBuffer```](https://www.mcbbs.net/thread-832205-1-1.html) [必须] 用于播放音乐，贴内有配套Mod 客户端需安装~~

## 1.4及以下版本使用

~~[```AudioBuffer```](https://www.mcbbs.net/thread-832205-1-1.html) [必须] 用于播放音乐，贴内有配套Mod 客户端需安装~~  
~~[```BossBarAPI```](https://www.mcbbs.net/thread-729531-1-1.html) [可选] 如需使用BossBar显示歌词 请安装~~  
~~[```ActionBarAPI```](https://www.spigotmc.org/resources/actionbarapi-1-8-1-14-2.1315/) [可选] 如需使用ActionBar显示歌词 请安装~~

# 搜索源说明

`搜索源` 为你要搜索音乐的平台  
目前支持以下平台

* qq - QQ音乐
* netease/163 - 网易云音乐
* kugou - 酷狗音乐
* kuwo - 酷我音乐
* bilibili - 哔哩哔哩音乐

# 歌名ID化说明

将歌名替换为 `-id:音乐ID` 即可  
目前支持 QQ 网易云 哔哩哔哩音乐  
示例: `/zm play bilibili -id:374305`


<span id="faq"></span>

# 常见问题

## 可能会遇到的问题

### 播放时没有声音

如果您使用的是`KCaldron`，那么很抱歉，我们暂不支持此服务器。

但如果您非要想使用模组服的话，这里有两种解决方案：

* 使用 [BungeeCord](https://www.spigotmc.org/wiki/bungeecord) 或者 [WaterFall](https://papermc.io/downloads#Waterfall)
  等群组服服务端。
* 使用 Arclight, LoliServer, Mohist, Uranium 等支持本插件的模组服核心。

如果您使用的是 **Spigot, Paper, Yatopia, Sugarcane 等原版插件服核心**，那么请检查您是否满足以下条件：

* 您的客户端已经安装了 AllMusic(1.7.10, 1.12或以上版本) 或者 AudioBuffer(1.12及以下版本)
* 您的服务端已正常安装了 ZMusic 插件
* 您在服务器插件文件夹安装的是 ZMusic 系列插件而不是 AllMusic 模组(模组需要在客户端进行安装)
* 您的客户端是 Fabric 或者 Forge 而**不是**纯净版，并且已经安装了 AudioBuffer **或者** AllMusic 模组
* 您的网络环境良好

如果你未满足以上条件中的其中一个的话，请进行进一步调整。

当然，如果您全部满足以上条件的话，还是无法播放音乐，我们建议您在未配置外置API的情况下使用**网易云音乐**进行播放。

实际上，我们建议您使用自己拥有的服务器开服，如果您使用的是**联云测等第三方服务平台**的话，可能会因为网络环境而不支持 ZMusic 长时间联网

### 服务器载入成功后，使用`/zm`相关指令时，会提示“错误：请等待插件载入完毕”

如果您遇到本问题的话，请升级 ZMusic 插件到最新版本即可解决问题。

### 插件无法载入，提示“请卸载AllMusic/AudioBuffer插件”

请在服务器插件文件夹删除AllMusic/AudioBuffer插件即可。  
在新版本的 ZMusic 中，我们弃用了 BossBarAPI, ActionBarAPI 依赖插件与 AudioBuffer 前置插件，为了防止服务器模组通讯频道冲突，我们不得不采取本措施。

### ZMusic 什么时候更新？

请加入测试群了解最新开发进度（群号951532728）

## 可能会遇到的后台异常

### 登录网易云音乐时，出现`java.net.UnknownHostException` 异常

您的配置文件在配置外置API的时候域名配置错误，请检查域名是否有效。   
如果域名有效的话，请检查您服务器的 DNS 是否正常或者刷新 DNS 缓存。  
面板服用户一般不会发生此种问题。

### 载入插件时，出现`java.lang.NoClassDefFoundError com/google/gson/xxx` 错误

您的服务端未内置gson库，安装带gson库的插件即可。

## 我找不到以上描述的任何错误

您可以加入我们的交流群，如果有报错信息的话，请上传后台报错信息至[Ubuntu PasteBin](https://paste.ubuntu.com/)  
然后通过链接的形式反馈给我们。  
帮助手册仍在进一步完善中！

## 安装 AllMusic 之后，客户端无法启动(Fabric)

* 请检查您安装的模组版本是否对应，如您的客户端使用的是Fabric模组加载器，但是您下载的是 `[Forge-1.16.5]-AllMusic-x.x.x.jar`
* 如果您下载的是Fabric版本模组，请检查您是否安装了`Fabric-API`前置模组
* 本模组与`CardBoard`不兼容

~~(所以我一个ZMusic使用文档为什么要回答AllMusic的问题)~~

## 是否支持 `1.7.10`

我们并不推荐您使用`1.7.10`版本, 如果您坚持使用, 请确保如下说明.

* 仅支持 `Mohist/Uranium` 服务端
* `Uranium` 仅在 `dev-4-b210` 测试成功
* `Mohist` 仅在 `1.7.10-42` 测试成功
* 除 `聊天信息` 外, 其他全部歌词显示均无效(除Uranium)
* 使用 `Uranium` 配套模组, 可实现Title/ActonBar显示
* 我们不接受任何 `1.7.10` 版本的问题反馈


[java]: https://img.shields.io/badge/java-1.8-blue

[kotlin]: https://img.shields.io/badge/kotlin-1.5.30-blue

[license]: https://img.shields.io/github/license/RealHeart/ZMusic?color=blue

[players]: https://img.shields.io/bstats/players/7291

[servers]: https://img.shields.io/bstats/servers/7291

[tested-versions]: https://img.shields.io/spiget/tested-versions/83027

[release]: https://img.shields.io/github/v/release/RealHeart/ZMusic

[downloads]: https://img.shields.io/github/downloads/RealHeart/ZMusic/total?color=blue
