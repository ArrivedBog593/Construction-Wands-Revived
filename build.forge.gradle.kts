plugins {
    id("java")
    id("net.minecraftforge.gradle") version "[6.0.24,6.2)"
}

fun prop(key: String): String = sc.properties[key]

version = "${prop("mod.version")}+${sc.current.version}"
base.archivesName = "${prop("mod.id")}-forge"

val requiredJava = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    else -> JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://maven.blamejared.com")
}

minecraft {
    mappings("official", sc.current.version)

    // Forge 1.20.6+ uses official mappings at runtime, so there is nothing to reobfuscate
    reobf = false

    // Calls processResources when in dev
    copyIdeResources = true

    runs {
        create("client") {
            workingDirectory(rootProject.file("run"))
            mods {
                create(prop("mod.id")) { source(sourceSets.main.get()) }
            }
        }

        create("server") {
            workingDirectory(rootProject.file("run"))
            mods {
                create(prop("mod.id")) { source(sourceSets.main.get()) }
            }
        }
    }
}

dependencies {
    val mc = sc.current.version

    "minecraft"("net.minecraftforge:forge:$mc-${prop("deps.forge")}")

    // Forge's hack fix
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4") { version { strictly("5.0.4") } }

    // Nullability annotations used by the shared code
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("org.jetbrains:annotations:24.1.0")

    // Recipe viewer (REI and EMI have no Forge build for this Minecraft version)
    compileOnly("mezz.jei:jei-$mc-common-api:${prop("deps.jei")}")
    compileOnly("mezz.jei:jei-$mc-forge-api:${prop("deps.jei")}")
    runtimeOnly("mezz.jei:jei-$mc-forge:${prop("deps.jei")}")
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
            "forge_version" to prop("deps.forge"),
            "forge_loader_version_range" to prop("deps.forge_loader_range"),
        )
        inputs.properties(props)

        filesMatching("META-INF/mods.toml") { expand(props) }

        exclude("fabric.mod.json", "META-INF/neoforge.mods.toml", "*.mixins.json")
    }

    // Includes the license file in the built mod
    withType<Jar> {
        val name = prop("mod.id")
        inputs.property("mod_id", name)
        from("../../LICENSE") { rename { "$it-$name" } }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds mod jars and copies results to `build/libs/{mod version}/`"

        inputs.property("version", prop("mod.version"))
        from(jar.flatMap { it.archiveFile }, named<Jar>("sourcesJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${prop("mod.version")}"))
    }
}
