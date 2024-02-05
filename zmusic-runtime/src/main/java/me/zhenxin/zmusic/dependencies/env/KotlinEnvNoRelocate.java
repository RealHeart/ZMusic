package me.zhenxin.zmusic.dependencies.env;

import me.zhenxin.zmusic.ZMusicConstants;
import me.zhenxin.zmusic.dependencies.annotation.RuntimeDependency;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib:" + ZMusicConstants.KOTLIN_VERSION,
        test = "!kotlin.KotlinVersion",
        initiative = true
)
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib-jdk7:" + ZMusicConstants.KOTLIN_VERSION,
        test = "!kotlin.jdk7.AutoCloseableKt",
        initiative = true
)
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib-jdk8:" + ZMusicConstants.KOTLIN_VERSION,
        test = "!kotlin.collections.jdk8.CollectionsJDK8Kt",
        initiative = true
)
public class KotlinEnvNoRelocate {
}