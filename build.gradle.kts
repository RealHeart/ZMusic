import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val zmusicAuthors = listOf("ZhenXin", "StarHui Technology")
val zmusicAuthor = zmusicAuthors.joinToString(", ")

extra["zmusicAuthor"] = zmusicAuthor
extra["zmusicAuthors"] = zmusicAuthors

fun gitCommitHash(): String {
    val process = ProcessBuilder("git", "rev-parse", "--short", "HEAD")
        .directory(rootDir)
        .redirectErrorStream(true)
        .start()
    val hash = process.inputReader().readText().trim()
    return if (process.waitFor() == 0 && hash.isNotEmpty()) ".$hash" else ""
}

plugins {
    java
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.gradleup.shadow) apply false
}

allprojects {
    repositories {
        maven {
            name = "PaperMC"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        maven {
            name = "PlaceholderAPI"
            url = uri("https://repo.extendedclip.com/releases/")
        }
        mavenCentral()
    }
}

subprojects {
    group = "me.zhenxin"
    version = "5.0.0-dev" + gitCommitHash()

    apply(plugin = "java-library")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.gradleup.shadow")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(8)
    }

    tasks.named("build") {
        dependsOn("shadowJar")
    }

    tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        manifest {
            attributes(
                "Implementation-Title" to "ZMusic",
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to zmusicAuthor
            )
        }
    }
}

tasks.jar { enabled = false }
