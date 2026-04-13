plugins {
    id("net.fabricmc.fabric-loom")
}

version = rootProject.property("version")!!
group = rootProject.property("group")!!

base {
    archivesName.set(rootProject.property("name")!!.toString())
}

repositories {
    maven("https://maven.terraformersmc.com/")
}

val minecraft = project.property("minecraft_version")!!
val loader = project.property("loader_version")!!
val modMenuVer = project.property("modmenu_version")!!

dependencies {
    minecraft("com.mojang:minecraft:${minecraft}")
    implementation("net.fabricmc:fabric-loader:${loader}")
    compileOnly("com.terraformersmc:modmenu:${modMenuVer}")
}

loom {
    mods {
        register("lysten") {
            sourceSet(sourceSets.main.get())
        }
    }
}

tasks {
    processResources {
        inputs.property("version", version)
        inputs.property("minecraft_version", minecraft)
        inputs.property("loader_version", loader)

        filesMatching("fabric.mod.json") {
            expand(
                "version" to version,
                "minecraft_version" to minecraft,
                "loader_version" to loader,
                "modmenu_version" to modMenuVer
            )
        }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName.get()}" }
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))

    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    withSourcesJar()
}