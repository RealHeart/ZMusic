repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly("com.mojang:brigadier:1.0.18")

    compileOnly("ink.ptms.core:v11902:11902:universal")
    compileOnly("ink.ptms.core:v11903:11903:universal")
    compileOnly("ink.ptms.core:v11904:11904:universal")
    compileOnly(project(":zmusic-nms:zmusic-nms-core"))
    compileOnly(libs.spigot)
}