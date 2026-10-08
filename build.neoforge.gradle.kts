plugins {
    id("net.neoforged.moddev") version "2.0.147"
    id("neoforge-mutex")
}

fun prop(key: String): String = sc.properties[key]
fun propOr(key: String, default: String): String = sc.properties.getOrNull<String>(key) ?: default

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

    /** Adds the dependency only if the version declares a version for [key]. */
    fun optional(key: String, add: (String) -> Unit) = sc.properties.getOrNull<String>(key)?.let(add)

    // Optional mods are only put on the dev runtime for versions that declare matching builds (deps.runtime)
    val runtimeMods = sc.properties.getOrNull<String>("deps.runtime") == "true"
    fun runtime(notation: String) = if (runtimeMods) runtimeOnly(notation) else compileOnly(notation)
    fun runtimeAndCompile(notation: String) = if (runtimeMods) implementation(notation) else compileOnly(notation)

    // Recipe viewers
    optional("deps.jei") {
        compileOnly("mezz.jei:jei-$mc-common-api:$it")
        compileOnly("mezz.jei:jei-$mc-neoforge-api:$it")
        if (runtimeMods) runtimeOnly("mezz.jei:jei-$mc-neoforge:$it")
    }
    optional("deps.rei") {
        compileOnly("me.shedaniel:RoughlyEnoughItems-api-neoforge:$it")
        compileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-neoforge:$it")
        if (runtimeMods) runtimeOnly("me.shedaniel:RoughlyEnoughItems-neoforge:$it")
    }
    optional("deps.emi") {
        compileOnly("dev.emi:emi-neoforge:$it:api")
        if (runtimeMods) runtimeOnly("dev.emi:emi-neoforge:$it")
    }

    // Storage mod integrations
    optional("deps.ae2") { runtimeAndCompile("org.appliedenergistics:appliedenergistics2:$it") }
    optional("deps.guideme") { runtimeAndCompile("org.appliedenergistics:guideme:$it") }
    optional("deps.refinedstorage") { runtimeAndCompile("com.refinedmods.refinedstorage:refinedstorage-neoforge:$it") }
    optional("deps.refinedstorage_curios") { runtimeAndCompile("curse.maven:refined-storage-curios-integration-1230729:$it") }
    optional("deps.sophisticatedbackpacks") { runtimeAndCompile("curse.maven:sophisticated-backpacks-422301:$it") }
    optional("deps.sophisticatedcore") { runtimeAndCompile("curse.maven:sophisticated-core-618298:$it") }

    // Curios and AE2 wireless terminals
    optional("deps.curios") {
        compileOnly("top.theillusivec4.curios:curios-neoforge:$it:api")
        if (runtimeMods) runtimeOnly("top.theillusivec4.curios:curios-neoforge:$it")
    }
    optional("deps.ae2wtlib") {
        runtimeAndCompile("de.mari_023:ae2wtlib_api:$it")
        if (runtimeMods) runtimeOnly("de.mari_023:ae2wtlib:$it")
    }
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
            val output = rootProject.file("generated/${sc.current.version}").absolutePath
            val existing = rootProject.file("src/main/resources").absolutePath
            programArguments.addAll("--mod", prop("mod.id"), "--all", "--output", output, "--existing", existing)
        }
    }
}

// Data generated for this Minecraft version (recipes, advancements, models, lang), see `generated/{version}`
sourceSets.main {
    resources.srcDir(rootProject.file("generated/${sc.current.version}"))
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
            "jei_version" to propOr("deps.jei", "0"),
            "guideme_version" to propOr("deps.guideme", "0"),
            "ae2_version" to propOr("deps.ae2", "0"),
            "refinedstorage_version" to propOr("deps.refinedstorage", "0"),
            "sophisticatedcore_version" to propOr("deps.sophisticatedcore", "0"),
            "sophisticatedbackpacks_version" to propOr("deps.sophisticatedbackpacks", "0"),
            "curios_version" to propOr("deps.curios", "0"),
            "ae2wtlib_version" to propOr("deps.ae2wtlib", "0"),
        )
        inputs.properties(props)

        filesMatching("META-INF/neoforge.mods.toml") { expand(props) }
        filesMatching("pack.mcmeta") { expand("pack_format" to prop("mod.pack_format")) }

        exclude("fabric.mod.json", "*.mixins.json")
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
