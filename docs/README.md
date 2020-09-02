# 介绍

## 构建状态
[![Build Status](https://ci.zhenxin.xyz/buildStatus/icon?job=Minecraft%2FZMusic)](https://ci.zhenxin.xyz/job/Minecraft/job/ZMusic/)

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

## 客户端Mod说明
本插件需要客户端安装配套Mod才能正常播放  
* 对于1.7.10-1.11的客户端 请安装[`AudioBuffer`](https://www.mcbbs.net/thread-832205-1-1.html) Mod
* 对于1.12.2-1.16.2的客户端 请安装[`AllMusic`](https://www.mcbbs.net/thread-972589-1-1.html) Mod

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

`/zm play`

### 参数
`搜索源` 要搜索音乐的平台  
目前支持以下平台  
* qq - QQ音乐  
* netease/163 - 网易云音乐  
* kugou - 酷狗音乐  
* kuwo - 酷我音乐  
* bilibili - 哔哩哔哩音乐  

`歌名` 要播放的歌名

### 示例
`/zm play qq 你的猫咪`

## 点歌

通过歌名搜索一个音乐，全服发送后，玩家点击播放

### 命令

`/zm music`

### 参数
`搜索源` 要搜索音乐的平台  
目前支持以下平台  
* qq - QQ音乐  
* netease/163 - 网易云音乐  
* kugou - 酷狗音乐  
* kuwo - 酷我音乐  
* bilibili - 哔哩哔哩音乐  

`歌名` 要点播的歌名

### 示例
`/zm music qq 你的猫咪`

## 搜索

通过歌名搜索一个音乐，返回十首音乐的列表

### 命令

`/zm search`

### 参数
`搜索源` 要搜索音乐的平台  
目前支持以下平台  
* qq - QQ音乐  
* netease/163 - 网易云音乐  
* kugou - 酷狗音乐  
* kuwo - 酷我音乐  
* bilibili - 哔哩哔哩音乐  

`歌名` 要搜索的歌名

### 示例
`/zm search qq 你的猫咪`

## 歌单

通过导入歌单存储在服务器，方便播放歌单。

### 命令

`/zm playlist`

### 参数

`平台/type` 要搜索音乐的平台  
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

### 主命令

`/zm`

### 子命令

`playAll` 强制全服播放
* 参数
   * `搜索源` 要搜索音乐的平台  
   目前支持以下平台  
     * qq - QQ音乐  
     * netease/163 - 网易云音乐  
     * kugou - 酷狗音乐  
     * kuwo - 酷我音乐  
     * bilibili - 哔哩哔哩音乐 
   * `歌名` 要播放的歌名

* 示例

`/zm playAll qq 你的猫咪`

`stopAll` 强制停止全服播放  
`reload` 重载配置文件  

# 权限

## 普通玩家权限

`zmusic.use` 可使用play,stop等普通指令`

## 管理员权限

`zmusic.admin` 可使用playAll,stopAll等管理员指令

# 配置文件

```yaml
# 配置文件版本(请勿修改)
version: 4
# 是否自动下载插件更新
update: false

# 是否开启调试模式
debug: false

# 账号设置
account:
  # 网易云音乐
  netease:
    # 登录方式
    #
    # email - 邮箱登录(密码处填写邮箱密码)
    # phone - 手机号登录(密码处填写网易云密码)
    loginType: phone
    # 账号
    #
    # 邮箱登录填写邮箱
    # 手机号登录填写手机号
    account: 18888888888
    # 密码
    #
    # 邮箱登录填写邮箱密码
    # 手机号登录填写网易云密码
    password: a123456
    # 密码方式
    #
    # normal = 纯密码 由插件通过md5加密上传到服务器验证
    # md5 = 纯md5 由用户通过md5加密设置在配置文件 插件读取上传到服务器验证
    passwordType: normal
    # 是否关注作者的网易云音乐账号
    follow: true
  # 哔哩哔哩
  bilibili:
    # 由于哔哩哔哩为m4a音频格式
    # 需要服务器用来转换
    # 因此哔哩哔哩播放功能收费5元/永久
    # 联系作者获取授权
    #
    # 授权QQ
    qq: 1307993674
    # 授权Key
    key: none

# 点歌设置
music:
  # 点歌扣除的金币(设置为0则不扣除)
  # 拥有zmusic.bypass的玩家无视扣除
  money: 10
  # 点歌的冷却时间(设置为0则无冷却)
  # 拥有zmusic.bypass的玩家无视冷却
  cooldown: 5

# 歌词设置
lyric:
  # 是否启用歌词
  enable: true
  # 是否显示歌词翻译
  showLyricTr: true
  # 以下为显示方式设置，可同时启用
  # 是否使用BossBar显示歌词(不支持1.8及以下)
  bossBar: true
  # 是否使用ActionBar显示歌词(不支持1.8及以下)
  actionBar: false
  # 是否使用Title显示歌词
  subTitle: false
  # 是否使用聊天信息显示歌词
  chatMessage: false
```

# 变量

`%zmusic_playing_name%` 获取当前播放的音乐名称  
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
