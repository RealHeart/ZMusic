plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("26.2.build.+")
    compileOnly(project(":zmusic-nms:zmusic-nms-core"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.reobfJar {
    // Build fails if enable
    enabled = false
}