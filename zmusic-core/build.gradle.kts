dependencies {
    api(project(":zmusic-runtime"))

    compileOnly(libs.bundles.okhttp)
    compileOnly(libs.bundles.fastjson)
    compileOnly(libs.bundles.nightconfig)
}

tasks.shadowJar {
    enabled = false
}