pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        mavenCentral()
    }

    plugins {
        id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT"
    }
}

rootProject.name = "Lysten"