dependencies {
    api(project(":zmusic-runtime"))

    compileOnly(libs.bundles.okhttp)
    compileOnly(libs.bundles.fastjson)
    compileOnly(libs.bundles.nightconfig)
}

blossom {
    val constants = "src/main/kotlin/me/zhenxin/zmusic/ZMusic.kt"
    replaceToken("#VERSION_NAME#", project.version, constants)
}

tasks.shadowJar {
    enabled = false
}