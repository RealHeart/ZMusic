dependencies {
    implementation("me.lucko:jar-relocator:1.7")
}

blossom {
    val kotlinVersion = kotlin.coreLibrariesVersion

    val runtimeEnv = "src/main/java/me/zhenxin/zmusic/dependencies/RuntimeEnv.java"
    val kotlinEnv = "src/main/java/me/zhenxin/zmusic/dependencies/env/KotlinEnv.java"
    val kotlinEnvNoRelocate = "src/main/java/me/zhenxin/zmusic/dependencies/env/KotlinEnvNoRelocate.java"
    replaceToken("#KOTLIN_VERSION#", kotlinVersion, runtimeEnv)
    replaceToken("#KOTLIN_VERSION#", kotlinVersion, kotlinEnv)
    replaceToken("#KOTLIN_VERSION#", kotlinVersion, kotlinEnvNoRelocate)

    val zmusicRuntime = "src/main/java/me/zhenxin/zmusic/ZMusicRuntime.java"
    replaceToken("#HUTOOL_VERSION#", libs.versions.hutool.get(), zmusicRuntime)
    replaceToken("#NETTY_VERSION#", libs.versions.netty.get(), zmusicRuntime)
    replaceToken("#NIGHT_CONFIG_VERSION#", libs.versions.nightconfig.get(), zmusicRuntime)
    replaceToken("#BSTATS_VERSION#", libs.versions.bstats.get(), zmusicRuntime)
}

tasks.shadowJar {
    enabled = false
}