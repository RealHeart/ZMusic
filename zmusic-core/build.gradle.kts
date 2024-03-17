dependencies {
    api(project(":zmusic-runtime"))

    compileOnly(libs.bundles.hutool)
    compileOnly(libs.bundles.nightconfig)
}

tasks.shadowJar {
    enabled = false
}