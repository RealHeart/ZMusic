# ZMusic

[![Build Status](https://ci.zhenxin.xyz/buildStatus/icon?job=Minecraft%2FZMusic)](https://ci.zhenxin.xyz/job/Minecraft/job/ZMusic/)

# 简介

这是一个功能强大的音乐系统，支持以下功能。
* 全服点歌
* 单独播放
* 歌词显示
* 多搜索源(QQ/网易云/酷狗/酷我)
* 音量调节(开发中...)
* 歌单播放(开发中...)
* 关键词搜索(开发中...)
* 循环整个歌单(开发中...)
* 个人歌单(开发中...)

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
示例：
/zm search 163 勾指起誓
```
* 管理员相关
```text
/zm admin playAll [搜索源] [歌名] - 强制全服玩家播放音乐
搜索源可为：
1.qq - QQ音乐
2.netease/163 - 网易云音乐
3.kugou - 酷狗音乐
4.kuwo - 酷我音乐
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
version: 1

# 是否开启调试模式
debug: false

# 点歌设置
music:
  # 点歌扣除的金币(设置为0则不扣除)
  # 拥有zmusic.bypass的玩家无视扣除
  money: 10 # 经济系统还没写 此条无效
  # 点歌的冷却时间(设置为0则无冷却)
  # 拥有zmusic.bypass的玩家无视冷却
  cooldown: 5 # 冷却系统还没写 此条无效

# 歌词设置
lyric:
  # 是否启用歌词
  enable: true
  # 以下为显示方式设置，可同时启用
  # 是否使用BossBar显示歌词(Mod服请关闭，不支持BossBar)
  bossBar: true
  # 是否使用ActionBar显示歌词
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