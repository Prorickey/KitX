plugins {
    id("java")
}

group = "xyz.prorickey"
version = "1.3.0"

tasks {
    assemble {
        dependsOn(":spigot:shadowJar")
        dependsOn(":api:jar")
    }
}