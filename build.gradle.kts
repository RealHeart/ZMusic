import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

allprojects {
    repositories {
        // 阿里云公共仓库
        maven("https://maven.aliyun.com/repository/public/")
        // CodeMC
        maven("https://repo.codemc.io/repository/nms-local/")
        // SpigotMC
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
        // Sponge
        maven("https://repo.spongepowered.org/maven")
        // Valocity
        maven("https://nexus.velocitypowered.com/repository/maven-public/")
    }

    group = "me.zhenxin"
    version = "3.0-21w26a"
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("com.github.johnrengelman.shadow")
    }
    dependencies {
        implementation(kotlin("stdlib"))
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "16"
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    tasks {
        val name = project.rootProject.name + "-" + project.name
        jar {
            archiveBaseName.set(name)
        }
        shadowJar {
            archiveBaseName.set(name)
        }
    }
    tasks.build {
        // build时执行shadowJar任务
        dependsOn(tasks.shadowJar)
    }
}

