dependencies {
    api(project(":zmusic-runtime"))

    compileOnly(libs.bundles.hutool)
    compileOnly(libs.nightconfig.yaml)
}

blossom {
    val constants = "src/main/kotlin/me/zhenxin/zmusic/ZMusic.kt"
    replaceToken("#VERSION_NAME#", project.version, constants)
}

tasks.shadowJar {
    enabled = false
}