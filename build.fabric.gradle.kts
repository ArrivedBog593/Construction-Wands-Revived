plugins {
    // This plugin applies the correct loom variant based on the Minecraft version
    id("dev.kikugie.loom-back-compat")
}

fun prop(key: String): String = sc.properties[key]

// DO NOT set group = ...!
version = "${prop("mod.version")}+${sc.current.version}"
base.archivesName = "${prop("mod.id")}-fabric"

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    else -> JavaVersion.VERSION_17
}

repositories {
    maven("https://maven.blamejared.com")
    maven("https://maven.shedaniel.me")
    maven("https://maven.terraformersmc.com/")
}

dependencies {
    val mc = sc.current.version

    minecraft("com.mojang:minecraft:$mc")
    // Applies Mojang Mappings on obfuscated versions
    loomx.applyMojangMappings()

    // Use `mod{dependency type}` even on 26.1+ - loom-back-compat converts them
    modImplementation("net.fabricmc:fabric-loader:${prop("deps.fabric_loader")}")
    // The full Fabric API
    modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric_api")}")

    // Nullability annotations used by the shared code
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("org.jetbrains:annotations:24.1.0")

    // Recipe viewers (compile-only, so the dev environment is not tied to their own dependencies)
    fun optional(key: String, add: (String) -> Unit) = sc.properties.getOrNull<String>(key)?.let(add)
    optional("deps.jei") {
        modCompileOnly("mezz.jei:jei-$mc-common-api-intermediary:$it") { isTransitive = false }
        modCompileOnly("mezz.jei:jei-$mc-fabric-api:$it")
    }
    optional("deps.rei") {
        modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:$it")
        modCompileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-fabric:$it")
    }
    optional("deps.emi") {
        modCompileOnly("dev.emi:emi-fabric:$it:api")
        modRuntimeOnly("dev.emi:emi-fabric:$it")
    }
}

loom {

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1") // Adds names to lambdas - useful for mixins
    }

    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        runDirectory = rootProject.file("run") // Shares the run directory between versions
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

/** Recipe viewer entrypoints, listed only for the viewers this version builds a plugin for. */
fun extraEntrypoints(): String {
    val entries = mutableListOf<String>()
    if (sc.properties.getOrNull<String>("deps.jei") != null) {
        entries += """"jei_mod_plugin": ["nadiendev.constructionwand.integrations.jei.ConstructionWandJeiPlugin"]"""
    }
    if (sc.properties.getOrNull<String>("deps.emi") != null) {
        entries += """"emi": ["nadiendev.constructionwand.integrations.emi.ConstructionWandEmiPlugin"]"""
    }
    if (sc.properties.getOrNull<String>("deps.rei") != null) {
        entries += """"rei_client": ["nadiendev.constructionwand.integrations.rei.ConstructionWandReiPlugin"]"""
    }
    return entries.joinToString("") { ",\n        $it" }
}

tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, value: String) {
            inputs.property(key, value)
            set(key, value)
        }

        val props = buildMap {
            register("mod_id", prop("mod.id"))
            register("mod_name", prop("mod.name"))
            register("mod_author", prop("mod.author"))
            register("version", prop("mod.version"))
            register("license", prop("mod.license"))
            register("description", prop("mod.description"))
            register("minecraft_version", prop("mod.mc_compat"))
            register("fabric_loader_version", Regex("\\d+\\.\\d+").find(prop("deps.fabric_loader"))!!.value)
            register("java_version", requiredJava.majorVersion)
            register("extra_entrypoints", extraEntrypoints())
        }

        filesMatching("fabric.mod.json") { expand(props) }
        filesMatching("pack.mcmeta") { expand("pack_format" to prop("mod.pack_format")) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }

        exclude("META-INF/neoforge.mods.toml")
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
        // loomx.mod(Sources)Jar returns the jar task for the applied loom variant
        from(loomx.modJar.flatMap { it.archiveFile }, loomx.modSourcesJar.flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${prop("mod.version")}"))
    }
}
