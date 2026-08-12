dependencies {
    api(project(":zmusic-runtime"))

    compileOnly(libs.bundles.nightconfig)
    compileOnly(libs.google.gson)
    compileOnly(libs.java.websocket)
    compileOnly(libs.slf4j.api)

    testImplementation(kotlin("test"))
    testImplementation(libs.google.gson)
    testImplementation(libs.java.websocket)
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    enabled = false
}
