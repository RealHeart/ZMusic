tasks.shadowJar {
    enabled = false
}

dependencies {
    api("net.kyori", "adventure-text-minimessage", "4.1.0-SNAPSHOT")
    api("com.alibaba", "fastjson", "1.2.76")
    api("com.squareup.okhttp3", "okhttp", "4.9.1")
    compileOnly("org.spigotmc", "spigot-api", "1.17-R0.1-SNAPSHOT")
    compileOnly("net.md-5", "bungeecord-api", "1.16-R0.5-SNAPSHOT")
}
