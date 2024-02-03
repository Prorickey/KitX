plugins {
    id("java")
}

group = "xyz.prorickey.kitx"
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    jar {
        destinationDirectory.set(file("${rootProject.rootDir}/output"))
        archiveClassifier.set("api")
        archiveBaseName.set("KitX")
    }
}