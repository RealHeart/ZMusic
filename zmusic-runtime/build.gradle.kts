import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.kyori.blossom)
}

dependencies {
    implementation("me.lucko:jar-relocator:1.7")
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("pluginVersion", project.version.toString())
                property("pluginVersionCode", versionCode())
                property("kotlinVersion", libs.versions.kotlin.get())
                property("okioVersion", libs.versions.okio.get())
                property("okhttpVersion", libs.versions.okhttp.get())
                property("fastjsonVersion", libs.versions.fastjson.get())
                property("nettyVersion", libs.versions.netty.get())
                property("nightConfigVersion", libs.versions.nightconfig.get())
                property("bstatsVersion", libs.versions.bstats.get())
            }
        }
    }
}

tasks.shadowJar {
    enabled = false
}

fun versionCode(): String {
    val runId = System.getenv("GITHUB_RUN_ID") ?: ""
    if (runId.isNotEmpty()) {
        return runId
    }

    val time = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyMMddHHmm")
    return time.format(formatter)
}