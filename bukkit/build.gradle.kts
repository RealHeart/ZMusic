repositories {
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
}

dependencies {
    api(project(":common"))
    compileOnly("org.spigotmc", "spigot-api", "1.17-R0.1-SNAPSHOT")
    // NMS
    compileOnly("org.bukkit", "craftbukkit", "1.12.2-R0.1-SNAPSHOT") // 1.12_R1
    compileOnly("org.bukkit", "craftbukkit", "1.13-R0.1-SNAPSHOT") // 1.13_R1
    compileOnly("org.bukkit", "craftbukkit", "1.13.2-R0.1-SNAPSHOT") // 1.13_R2
    compileOnly("org.bukkit", "craftbukkit", "1.14.4-R0.1-SNAPSHOT") // 1.14_R1
    compileOnly("org.bukkit", "craftbukkit", "1.15.2-R0.1-SNAPSHOT") // 1.15_R1
    compileOnly("org.bukkit", "craftbukkit", "1.16.1-R0.1-SNAPSHOT") // 1.16_R1
    compileOnly("org.bukkit", "craftbukkit", "1.16.2-R0.1-SNAPSHOT") // 1.16_R2
    compileOnly("org.bukkit", "craftbukkit", "1.16.5-R0.1-SNAPSHOT") // 1.16_R3
    compileOnly("org.bukkit", "craftbukkit", "1.17-R0.1-SNAPSHOT") // 1.17_R1
}
