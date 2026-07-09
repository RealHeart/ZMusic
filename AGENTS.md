# AGENTS.md

此文件为 AI 智能体提供 ZMusic 项目开发指导。

## 项目概览

ZMusic 是 Minecraft 音乐系统服务端插件，使用 Kotlin + Gradle 多模块实现，目标是统一 Bukkit、BungeeCord、Velocity 三个平台的核心逻辑。

## 关键约束

- 始终使用中文回复。
- 项目源码保持 Kotlin，不新增 Java 源码，除非用户明确要求。
- 构建使用 JDK 25。
- 产物最低运行兼容 Java 8，Kotlin `jvmTarget` 保持 `1.8`，Java `release` 保持 `8`。
- 不使用 Unsafe 或反射修改当前插件 classpath。
- Kotlin runtime 内置；Night Config 等业务依赖由 runtime 动态加载。
- 注释使用 KDoc，类型级注释包含 `@author` 和 `@since`，作者按实际贡献者填写。
- 如有必须，需要更新 `AGENTS.md` 的内容。

## 模块结构

```text
zmusic-runtime   运行时依赖下载、隔离 classloader、平台公共接口
zmusic-core      平台无关核心逻辑、命令、配置、播放服务
zmusic-bukkit    Bukkit / Spigot / Paper / Folia 入口和适配层
zmusic-bungee    BungeeCord 入口和适配层
zmusic-velocity  Velocity 入口和适配层
```

## 构建命令

```bash
./gradlew build
./gradlew :zmusic-bukkit:shadowJar
./gradlew :zmusic-bungee:shadowJar
./gradlew :zmusic-velocity:shadowJar
```

## 重要文件

- `build.gradle.kts`：统一版本、JDK toolchain、Java 8 字节码目标、Shadow Jar 配置。
- `gradle/libs.versions.toml`：依赖版本集中管理。
- `zmusic-runtime/src/main/kotlin/me/zhenxin/zmusic/runtime/ZMusicRuntime.kt`：runtime 启动入口。
- `zmusic-runtime/src/main/kotlin/me/zhenxin/zmusic/runtime/IsolatedRuntimeClassLoader.kt`：隔离类加载器。
- `zmusic-core/src/main/kotlin/me/zhenxin/zmusic/ZMusicApplication.kt`：core 应用入口。
- `zmusic-bukkit/src/main/resources/plugin.yml`：Bukkit 插件元数据。
- `zmusic-bungee/src/main/resources/bungee.yml`：BungeeCord 插件元数据。
- `zmusic-velocity/src/main/resources/velocity-plugin.json`：Velocity 插件元数据。

## 运行时依赖加载

runtime 会把动态依赖下载到插件数据目录的 `libraries/` 下，然后用 `IsolatedRuntimeClassLoader` 加载 core 和依赖。不要把依赖追加到当前插件 classloader。

当前动态依赖：

- `com.electronwill.night-config:core`
- `com.electronwill.night-config:toml`

## 注释规范

- 类、接口、对象、data class 使用 KDoc。
- 类型级 KDoc 包含 `@author` 和 `@since`，作者按实际贡献者填写。
- 函数有参数时写 `@param`。
- 函数有返回值时写 `@return`。
- 函数主动抛出或包装异常时写 `@throws`。
- 注释只解释代码职责和不直观逻辑，不写长篇设计讨论。

## 验证

修改后至少运行：

```bash
./gradlew build
```

构建可能出现 BungeeCord `sendMessage(String)` 的 deprecated warning；该 warning 当前不影响产物。

## 开源协议

项目使用 GPL-3.0，协议正文见 `LICENSE`。
