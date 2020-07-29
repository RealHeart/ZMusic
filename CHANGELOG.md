# 0.10-beta | 2020-07-28
* 修复播放歌单时最大时间均为第一首的问题。
* 引入网易云加密算法。
* 修复歌单只能导入7首左右的问题。
  * 由于更换歌单获取接口，导入速度有所下降。
  * 由于更换歌单获取接口，可能出现导入返回空。

# 0.9-beta | 2020-06-20
* 修复使用/zm search时，网易云音乐点歌和搜索结果不同的问题
* 优化获取音乐持续时间的代码
* PS: 此版本需要重新导入歌单数据，否则无法使用。
* PS: 歌单只导入6-10首为网易云问题，暂时无法修复。

# 0.8-beta2 | 2020-06-03
* 修复/zm music指令重复发送问题

# 0.8-beta | 2020-06-03
* 修复/zm music指令无效问题

# 0.7-beta | 2020-05-21
* 修复playAll,stopAll命令控制台使用报错的问题
* 修复1.7.10使用/zm music指令报错的问题

# 0.6-beta | 2020-04-26
* 修复playAll,stopAll命令无效问题
* 添加经济系统
* 添加冷却系统

# 0.5-beta | 2020-04-24
* 修复循环播放出错问题
* 优化TAB补全功能
* 添加歌单功能(仅支持网易云音乐)
* 添加bStats统计

# 0.4-beta | 2020-03-12
* 添加变量%zmusic_status_play% - 获取播放状态
* 添加变量%zmusic_status_loop% - 获取循环状态
* 修复在1.8以下服务端发送Title异常的问题。

# 0.3-beta | 2020-03-05
* 添加/zm search命令 - 用于搜索音乐列表
* |--> 暂时显示前十条搜索结果，后续添加翻页
* 优化Tab补全功能
* 优化检测更新功能

# 0.2-beta | 2020-02-28
* 优化配置加载
* 添加点歌显示平台

# 0.1-beta | 2020-02-27
* 第一个版本