plugins {
    id("net.neoforged.moddev") version "2.0.147"
}

fun propOrNull(key: String): String? = findProperty(key)?.toString()
fun prop(key: String): String = propOrNull(key) ?: error("Missing property $key")
fun propOr(key: String, default: String): String = propOrNull(key) ?: default

val mc = prop("minecraft_version")

/** Compares dotted Minecraft versions numerically, e.g. `mcAtLeast("1.21.4")`. */
fun mcAtLeast(other: String): Boolean {
    val a = mc.split('.').map { it.toInt() }
    val b = other.split('.').map { it.toInt() }
    for (i in 0 until maxOf(a.size, b.size)) {
        val diff = a.getOrElse(i) { 0 } - b.getOrElse(i) { 0 }
        if (diff != 0) return diff > 0
    }
    return true
}

val common = project(":common")

version = "${prop("mod.version")}+$mc"
base.archivesName = "${prop("mod.id")}-neoforge"

val requiredJava = when {
    mcAtLeast("26.1") -> JavaVersion.VERSION_25
    mcAtLeast("1.20.5") -> JavaVersion.VERSION_21
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
    maven("https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
    maven("https://modmaven.dev/") {
        content {
            includeGroup("appeng")
            includeGroup("de.mari_023")
        }
    }
}

dependencies {
    // JEI skips some Minecraft versions; its API is compatible with the neighbouring build
    val jeiMc = propOrNull("deps.jei_mc") ?: mc

    /** Adds the dependency only if the version declares a version for [key]. */
    fun optional(key: String, add: (String) -> Unit) = propOrNull(key)?.let(add)

    // Optional mods are only put on the dev runtime for versions that declare matching builds (deps.runtime)
    val runtimeMods = propOrNull("deps.runtime") == "true"
    fun runtime(notation: String) = if (runtimeMods) runtimeOnly(notation) else compileOnly(notation)
    fun runtimeAndCompile(notation: String) = if (runtimeMods) implementation(notation) else compileOnly(notation)

    // Recipe viewers
    optional("deps.jei") {
        compileOnly("mezz.jei:jei-$jeiMc-common-api:$it")
        compileOnly("mezz.jei:jei-$jeiMc-neoforge-api:$it")
        if (runtimeMods) runtimeOnly("mezz.jei:jei-$jeiMc-neoforge:$it")
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
    // CurseForge file ids for the older builds, Modrinth version numbers (e.g. 26.2-3.26.9.2191) for the newer ones
    fun sophisticated(key: String, slug: String, curseProject: String) = optional(key) {
        if (it.all(Char::isDigit)) runtimeAndCompile("curse.maven:$slug-$curseProject:$it")
        else runtimeAndCompile("maven.modrinth:$slug:$it")
    }
    sophisticated("deps.sophisticatedbackpacks", "sophisticated-backpacks", "422301")
    sophisticated("deps.sophisticatedcore", "sophisticated-core", "618298")

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

        // 1.21.4 split the data run into client and server flavours; the client one also covers the server data
        register(if (mcAtLeast("1.21.4")) "clientData" else "data") {
            if (mcAtLeast("1.21.4")) clientData() else data()
            val output = common.file("src/generated/resources").absolutePath
            val existing = common.file("src/main/resources").absolutePath
            programArguments.addAll("--mod", prop("mod.id"), "--all", "--output", output, "--existing", existing)
        }
    }
}

// Data generated for this Minecraft version (recipes, advancements, models, lang), see `common/src/generated`
sourceSets.main {
    java.srcDir(common.file("src/main/java"))
    resources.srcDir(common.file("src/main/resources"))
    resources.srcDir(common.file("src/generated/resources"))
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
