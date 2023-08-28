package me.zhenxin.zmusic.dependencies.env;

import me.zhenxin.zmusic.dependencies.annotation.RuntimeDependency;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib:#KOTLIN_VERSION#",
        test = "!kotlin@kotlin_version_escape@.KotlinVersion",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."},
        initiative = true
)
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib-jdk7:#KOTLIN_VERSION#",
        test = "!kotlin@kotlin_version_escape@.jdk7.AutoCloseableKt",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."},
        initiative = true
)
@RuntimeDependency(
        value = "!org.jetbrains.kotlin:kotlin-stdlib-jdk8:#KOTLIN_VERSION#",
        test = "!kotlin@kotlin_version_escape@.collections.jdk8.CollectionsJDK8Kt",
        relocate = {"!kotlin.", "!me.zhenxin.zmusic.library.kotlin."},
        initiative = true
)
public class KotlinEnv {
}