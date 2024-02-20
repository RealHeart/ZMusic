package me.zhenxin.zmusic.dependencies.env;

import me.zhenxin.zmusic.ZMusicConstants;
import me.zhenxin.zmusic.dependencies.annotation.RuntimeDependency;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib:" + ZMusicConstants.KOTLIN_VERSION,
        test = "!me.zhenxin.zmusic.library.kotlin.KotlinVersion",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."},
        initiative = true
)
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib-jdk7:" + ZMusicConstants.KOTLIN_VERSION,
        test = "!me.zhenxin.zmusic.library.kotlin.jdk7.AutoCloseableKt",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."},
        initiative = true
)
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib-jdk8:" + ZMusicConstants.KOTLIN_VERSION,
        test = "!me.zhenxin.zmusic.library.kotlin.collections.jdk8.CollectionsJDK8Kt",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."},
        initiative = true
)
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-reflect:" + ZMusicConstants.KOTLIN_VERSION,
        test = "!me.zhenxin.zmusic.library.kotlin.reflect.KotlinReflect",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."},
        initiative = true
)
public class KotlinEnv {
}