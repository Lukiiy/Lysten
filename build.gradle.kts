import org.slf4j.event.Level

plugins {
    id("net.neoforged.moddev") version "2.0.141"
}

version = rootProject.property("version")!!
group = rootProject.property("group")!!

val neoVersion = rootProject.property("neo_version")!!
val id = rootProject.property("id")!!.toString()

base.archivesName.set(id)
java.toolchain.languageVersion.set(JavaLanguageVersion.of(25))

neoForge {
    version = neoVersion.toString()

    runs {
        create("client") {
            client()

            systemProperty("neoforge.enabledGameTestNamespaces", id)
        }

        create("data") {
            clientData()

            programArguments.addAll(listOf("--mod", id, "--all", "--output", file("src/generated/resources").absolutePath, "--existing", file("src/main/resources").absolutePath))
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")

            logLevel = Level.DEBUG
        }
    }

    mods {
        create(id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets {
    named("main") {
        resources.srcDir("src/generated/resources")
    }
}

val generateModMetadata by tasks.registering(ProcessResources::class) {
    val replace = mapOf(
        "minecraft_version" to rootProject.property("minecraft_version")!!,
        "minecraft_version_range" to rootProject.property("minecraft_version_range")!!,
        "neo_version" to neoVersion,
        "neo_version_range" to rootProject.property("neo_version_range")!!,
        "loader_version_range" to rootProject.property("loader_version_range")!!,
        "mod_id" to id,
        "mod_name" to rootProject.property("name")!!.toString(),
        "mod_license" to rootProject.property("license")!!.toString(),
        "mod_version" to version,
        "mod_authors" to rootProject.property("authors")!!,
        "mod_description" to rootProject.property("description")!!.toString()
    )

    inputs.properties(replace)

    from("src/main/templates")
    into("build/generated/sources/modMetadata")
    expand(replace)
}

sourceSets {
    named("main") {
        resources.srcDir(generateModMetadata)
    }
}

neoForge.ideSyncTask(generateModMetadata)

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}