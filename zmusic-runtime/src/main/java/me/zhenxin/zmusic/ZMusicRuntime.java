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
        value = "!com.squareup.okio:okio-jvm:#OKIO_VERSION#",
        test = "!okio.Buffer",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."}
)
@RuntimeDependency(
        value = "!com.squareup.okhttp3:okhttp:#OKHTTP_VERSION#",
        test = "!okhttp3.OkHttpClient",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."}
)
@RuntimeDependency(
        value = "!com.alibaba.fastjson2:fastjson2:#FASTJSON_VERSION#",
        test = "!com.alibaba.fastjson2.JSON"
)
@RuntimeDependency(
        value = "!com.alibaba.fastjson2:fastjson2-kotlin:#FASTJSON_VERSION#",
        test = "!com.alibaba.fastjson2.JSONKt",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."}
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
@RuntimeDependency(
        value = "!org.bstats:bstats-base:#BSTATS_VERSION#",
        test = "!me.zhenxin.zmusic.library.bstats.MetricsBase",
        relocate = {"!org.bstats.", "!me.zhenxin.zmusic.library.bstats."}
)
public class ZMusicRuntime {

    public static void setup(String dataFolder, Logger logger, Class<?>... classes) {
        logger.info("Loading libraries, please wait...");
        RuntimeEnv.ENV.setup(dataFolder);
        RuntimeEnv.ENV.inject(ZMusicRuntime.class);
        for (Class<?> clazz : classes) {
            RuntimeEnv.ENV.inject(clazz);
        }
        logger.info("Libraries loaded.");
    }
}
