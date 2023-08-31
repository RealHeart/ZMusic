package me.zhenxin.zmusic;

import me.zhenxin.zmusic.dependencies.RuntimeEnv;
import me.zhenxin.zmusic.dependencies.annotation.RuntimeDependency;

import java.util.logging.Logger;

/**
 * ZMusic 运行时依赖
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2023/7/24 18:14
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@RuntimeDependency(
        value = "!cn.hutool:hutool-core:#HUTOOL_VERSION#",
        test = "!cn.hutool.core.util.ArrayUtil"
)
@RuntimeDependency(
        value = "!cn.hutool:hutool-http:#HUTOOL_VERSION#",
        test = "!cn.hutool.http.HttpUtil"
)
@RuntimeDependency(
        value = "!cn.hutool:hutool-json:#HUTOOL_VERSION#",
        test = "!cn.hutool.json.JSONUtil"
)
@RuntimeDependency(
        value = "!io.netty:netty-buffer:#NETTY_VERSION#",
        test = "!io.netty.buffer.ByteBuf"
)
@RuntimeDependency(
        value = "!com.electronwill.night-config:core:#NIGHT_CONFIG_VERSION#",
        test = "!com.electronwill.nightconfig.core.Config"
)
@RuntimeDependency(
        value = "!com.electronwill.night-config:toml:#NIGHT_CONFIG_VERSION#",
        test = "!com.electronwill.nightconfig.toml.TomlFormat"
)
public class ZMusicRuntime {

    public static void setup(String dataFolder, Logger logger) {
        logger.info("Loading libraries, please wait...");
        RuntimeEnv.ENV.setup(dataFolder);
        RuntimeEnv.ENV.inject(ZMusicRuntime.class);
        logger.info("Libraries loaded.");
    }
}
