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
    // The full Fabric API, so its version also wins over the one Forge Config API Port depends on
    modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric_api")}")

    // Nullability annotations used by the shared code
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("org.jetbrains:annotations:24.1.0")

    // Recipe viewers
    modCompileOnly("mezz.jei:jei-$mc-common-api-intermediary:${prop("deps.jei")}") { isTransitive = false }
    modCompileOnly("mezz.jei:jei-$mc-fabric-api:${prop("deps.jei")}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:${prop("deps.rei")}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-fabric:${prop("deps.rei")}")
    modCompileOnly("dev.emi:emi-fabric:${prop("deps.emi")}:api")
    modRuntimeOnly("dev.emi:emi-fabric:${prop("deps.emi")}")
}

loom {
    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json")

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
        }

        filesMatching("fabric.mod.json") { expand(props) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }

        exclude("META-INF/neoforge.mods.toml", "META-INF/mods.toml")
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
