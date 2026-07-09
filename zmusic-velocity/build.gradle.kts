import org.gradle.api.attributes.java.TargetJvmVersion

dependencies {
    implementation(project(":zmusic-core"))

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}

configurations.configureEach {
    if (name == "compileClasspath" || name == "annotationProcessor") {
        attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 17)
    }
}

tasks.processResources {
    inputs.property("version", version)
    inputs.property("author", rootProject.extra["zmusicAuthor"])
    inputs.property("organization", (rootProject.extra["zmusicAuthors"] as List<*>)[1])

    filesMatching("velocity-plugin.json") {
        expand(
            mapOf(
                "version" to version,
                "author" to (rootProject.extra["zmusicAuthors"] as List<*>)[0],
                "organization" to (rootProject.extra["zmusicAuthors"] as List<*>)[1]
            )
        )
    }
}
