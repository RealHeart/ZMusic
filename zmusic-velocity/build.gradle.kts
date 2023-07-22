repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":zmusic-common"))
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
}