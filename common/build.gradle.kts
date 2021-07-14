tasks.shadowJar {
    enabled = false
}

dependencies {
    api("net.kyori", "adventure-text-minimessage", "4.1.0-SNAPSHOT")
    api("com.alibaba", "fastjson", "1.2.76")
    compileOnly("org.yaml", "snakeyaml", "1.29")
}
