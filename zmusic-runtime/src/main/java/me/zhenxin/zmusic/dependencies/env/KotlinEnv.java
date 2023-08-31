package me.zhenxin.zmusic.dependencies.env;

import me.zhenxin.zmusic.dependencies.annotation.RuntimeDependency;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib:#KOTLIN_VERSION#",
        test = "!me.zhenxin.zmusic.library.kotlin.KotlinVersion",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."},
        initiative = true
)
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib-jdk7:#KOTLIN_VERSION#",
        test = "!me.zhenxin.zmusic.library.kotlin.jdk7.AutoCloseableKt",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."},
        initiative = true
)
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib-jdk8:#KOTLIN_VERSION#",
        test = "!me.zhenxin.zmusic.library.kotlin.collections.jdk8.CollectionsJDK8Kt",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."},
        initiative = true
)
public class KotlinEnv {
}