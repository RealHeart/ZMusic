dependencies {
    api(project(":zmusic-runtime"))

    compileOnly(libs.bundles.nightconfig)
}

tasks.shadowJar {
    enabled = false
}
