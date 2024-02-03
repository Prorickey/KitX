plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "xyz.prorickey.kitx"
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.cybercake.net/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    implementation(project(":api"))
    implementation("me.lucko:commodore:1.9")
    implementation("cloud.commandframework", "cloud-paper", "1.8.4")
    implementation("net.cybercake.cyberapi:common:171")
    implementation("net.cybercake.cyberapi:spigot:171")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    jar {
        enabled = false
        dependsOn("shadowJar")
    }

    shadowJar {
        destinationDirectory.set(file("${rootProject.rootDir}/output"))
        archiveClassifier.set("spigot")
        archiveBaseName.set("KitX")
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}