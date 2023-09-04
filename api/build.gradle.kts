plugins {
    id("java")
}

group = "xyz.prorickey.kitx"
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {

}

tasks {
    jar {
        destinationDirectory.set(file("${rootProject.rootDir}/output"))
        archiveClassifier.set("api")
        archiveBaseName.set("KitX")
    }
}