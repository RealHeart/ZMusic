@file:Suppress("SpellCheckingInspection")

version = "3.1.0"

repositories {
    // TabooLib
    maven("https://repo.tabooproject.org/repository/releases")
}

dependencies {
    libs.bundles.nms.get().forEach {
        compileOnly(
            group = it.module.group,
            name = it.module.name,
            version = it.versionConstraint.toString(),
            classifier = "universal"
        )
    }
    compileOnly(libs.nms.legacy)

    compileOnly(libs.spigot)

    compileOnly(libs.placeholderapi)
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("plugin.yml") {
        expand(mapOf("version" to version))
    }
}

tasks.jar {
    archiveBaseName.set("ZMusicBridge")
}
