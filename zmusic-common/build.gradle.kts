dependencies {
    implementation("cn.hutool:hutool-http:5.8.16")
    implementation("cn.hutool:hutool-json:5.8.16")
}

tasks.shadowJar {
    enabled = false
}