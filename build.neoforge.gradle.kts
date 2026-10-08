plugins {
    id("net.neoforged.moddev") version "2.0.147"
    id("neoforge-mutex")
}

fun prop(key: String): String = sc.properties[key]

version = "${prop("mod.version")}+${sc.current.version}"
base.archivesName = "${prop("mod.id")}-neoforge"

val requiredJava = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    else -> JavaVersion.VERSION_17
}

repositories {
    maven("https://maven.blamejared.com")
    maven("https://maven.theillusivec4.top/")
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.shedaniel.me")
    maven("https://maven.creeperhost.net") {
        content { includeGroup("com.refinedmods.refinedstorage") }
    }
    maven("https://cursemaven.com") {
        name = "Curse Maven"
        content { includeGroup("curse.maven") }
    }
    maven("https://modmaven.dev/") {
        content {
            includeGroup("appeng")
            includeGroup("de.mari_023")
        }
    }
}

dependencies {
    val mc = sc.current.version

    // Recipe viewers
    compileOnly("mezz.jei:jei-$mc-common-api:${prop("deps.jei")}")
    compileOnly("mezz.jei:jei-$mc-neoforge-api:${prop("deps.jei")}")
    runtimeOnly("mezz.jei:jei-$mc-neoforge:${prop("deps.jei")}")

    compileOnly("me.shedaniel:RoughlyEnoughItems-api-neoforge:${prop("deps.rei")}")
    compileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-neoforge:${prop("deps.rei")}")
    runtimeOnly("me.shedaniel:RoughlyEnoughItems-neoforge:${prop("deps.rei")}")

    compileOnly("dev.emi:emi-neoforge:${prop("deps.emi")}:api")
    runtimeOnly("dev.emi:emi-neoforge:${prop("deps.emi")}")

    // Storage mod integrations
    compileOnly("org.appliedenergistics:appliedenergistics2:${prop("deps.ae2")}")
    implementation("org.appliedenergistics:appliedenergistics2:${prop("deps.ae2")}")
    implementation("org.appliedenergistics:guideme:${prop("deps.guideme")}")
    implementation("com.refinedmods.refinedstorage:refinedstorage-neoforge:${prop("deps.refinedstorage")}")
    implementation("curse.maven:refined-storage-curios-integration-1230729:${prop("deps.refinedstorage_curios_id")}")
    implementation("curse.maven:sophisticated-backpacks-422301:${prop("deps.sophisticatedbackpacks_id")}")
    implementation("curse.maven:sophisticated-core-618298:${prop("deps.sophisticatedcore_id")}")

    // Curios and AE2 wireless terminals
    compileOnly("top.theillusivec4.curios:curios-neoforge:${prop("deps.curios")}:api")
    runtimeOnly("top.theillusivec4.curios:curios-neoforge:${prop("deps.curios")}")
    implementation("de.mari_023:ae2wtlib_api:${prop("deps.ae2wtlib")}")
    runtimeOnly("de.mari_023:ae2wtlib:${prop("deps.ae2wtlib")}")
}

neoForge {
    version = prop("deps.neo_loader")

    mods {
        register(prop("mod.id")) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            gameDirectory = rootProject.file("run")
            client()
        }

        register("server") {
            gameDirectory = rootProject.file("run")
            server()
            programArgument("--nogui")
        }

        register("data") {
            data()
            val resources = rootProject.file("src/main/resources").absolutePath
            programArguments.addAll("--mod", prop("mod.id"), "--all", "--output", resources, "--existing", resources)
        }
    }
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        vendor = JvmVendorSpec.ADOPTIUM
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

tasks {
    processResources {
        val props = mapOf(
            "mod_id" to prop("mod.id"),
            "mod_name" to prop("mod.name"),
            "mod_author" to prop("mod.author"),
            "version" to prop("mod.version"),
            "license" to prop("mod.license"),
            "description" to prop("mod.description"),
            "credits" to "",
            "minecraft_version_range" to prop("mod.mc_compat"),
            "neoforge_version" to prop("deps.neo_loader"),
            "neoforge_loader_version_range" to prop("deps.neo_loader_range"),
            "jei_version" to prop("deps.jei"),
            "guideme_version" to prop("deps.guideme"),
            "ae2_version" to prop("deps.ae2"),
            "refinedstorage_version" to prop("deps.refinedstorage"),
            "sophisticatedcore_version" to prop("deps.sophisticatedcore_id"),
            "sophisticatedbackpacks_version" to prop("deps.sophisticatedbackpacks_id"),
            "curios_version" to prop("deps.curios"),
            "ae2wtlib_version" to prop("deps.ae2wtlib"),
        )
        inputs.properties(props)

        filesMatching("META-INF/neoforge.mods.toml") { expand(props) }

        exclude("fabric.mod.json", "META-INF/mods.toml", "*.mixins.json")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    // Includes the license file in the built mod
    withType<Jar> {
        val name = prop("mod.id")
        inputs.property("mod_id", name)
        from(rootProject.file("LICENSE")) { rename { "$it-$name" } }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds mod jars and copies results to `build/libs/{mod version}/`"

        inputs.property("version", prop("mod.version"))
        from(jar.flatMap { it.archiveFile }, named<Jar>("sourcesJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${prop("mod.version")}"))
    }
}
