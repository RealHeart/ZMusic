# ZMusic

[![Build Status](https://ci.zhenxin.xyz/buildStatus/icon?job=Minecraft%2FZMusic)](https://ci.zhenxin.xyz/job/Minecraft/job/ZMusic/)

# 简介

这是一个功能强大的音乐系统，支持以下功能。
* 全服点歌
* 单独播放
* 歌词显示
* 歌词翻译显示(目前仅支持QQ音乐)
* 多搜索源(QQ/网易云/酷狗/酷我)
* 关键词搜索
* 个人歌单
* 歌单播放
* 音量调节(开发中...)
* 循环整个歌单(开发中...)

# 视频
[点击前往哔哩哔哩查看演示视频](https://www.bilibili.com/video/av92156922)

# 反馈
* 提交 [Issues](../../issues)
* 加入交流群：[1032722724](https://jq.qq.com/?_wv=1027&k=5oIs7cc) 反馈

# 命令
* 主命令

```text
/zm - 主命令
/zm help - 查看帮助
/zm play - 播放音乐
/zm music - 全服点歌
```

* 播放相关

```text
/zm play [搜索源] [歌名] - 播放音乐
搜索源可为：
1.qq - QQ音乐
2.netease/163 - 网易云音乐
3.kugou - 酷狗音乐
4.kuwo - 酷我音乐
5.bilibili - 哔哩哔哩音乐
示例：
/zm play 163 勾指起誓
```

* 点歌相关

```text
/zm music [搜索源] [歌名] - 播放音乐
搜索源可为：
1.qq - QQ音乐
2.netease/163 - 网易云音乐
3.kugou - 酷狗音乐
4.kuwo - 酷我音乐
5.bilibili - 哔哩哔哩音乐
示例：
/zm music 163 勾指起誓
```

* 搜索相关

```text
/zm search [搜索源] [歌名] - 搜索音乐
搜索源可为：
1.qq - QQ音乐
2.netease/163 - 网易云音乐
3.kugou - 酷狗音乐
4.kuwo - 酷我音乐
5.bilibili - 哔哩哔哩音乐
示例：
/zm search 163 勾指起誓
```

* 歌单相关

```text
/zm playlist import <歌单链接> - 导入歌单
- 示例：/zm playlist import https://music.163.com/#/playlist?id=363046232
/zm playlist list - 查看已导入的歌单列表
/zm playlist play <歌单ID> - 播放已导入的歌单(可查看列表直接点击播放)
- 示例：/zm playlist play 363046232
```

* 管理员相关

```text
/zm admin playAll [搜索源] [歌名] - 强制全服玩家播放音乐
搜索源可为：
1.qq - QQ音乐
2.netease/163 - 网易云音乐
3.kugou - 酷狗音乐
4.kuwo - 酷我音乐
5.bilibili - 哔哩哔哩音乐
示例：
/zm playAll 163 勾指起誓
------------------------
/zm stopAll - 强制停止全部玩家播放音乐
/zm reload - 重载配置文件
```

#权限
* 普通玩家权限

```text
zmusic.use - 可使用play,stop等普通指令
```

* 管理员权限

```text
zmusic.admin - 可使用playAll,stopAll等管理员指令
```

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
  # 是否使用BossBar显示歌词(Mod服请关闭，不支持BossBar)
  # 需要前置插件BossBarAPI
  bossBar: true
  # 是否使用ActionBar显示歌词
  # 需要前置插件ActionBarAPI
  actionBar: false
  # 是否使用Title显示歌词
  subTitle: false
  # 是否使用聊天信息显示歌词
  chatMessage: false
```

# 变量
* 需要PlaceholderAPI

```text
%zmusic_playing_name% - 获取当前播放的音乐名称
%zmusic_playing_lyric% - 获取当前时间显示的歌词
%zmusic_time_current% - 获取当前播放的音乐的时间
%zmusic_time_max% - 获取当前播放的音乐的最大时间
```