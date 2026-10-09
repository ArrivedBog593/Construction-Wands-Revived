pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "FabricMC" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    // https://stonecutter.kikugie.dev/blog/changes/0.9
    id("dev.kikugie.stonecutter") version "0.9.8"

    // Applies the right Loom variant for obfuscated and unobfuscated Minecraft versions
    id("dev.kikugie.loom-back-compat") version "0.4.2"

    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        /**
         * Creates one node per loader for a Minecraft version: `loaders/{loader}`.
         * Every node uses `build.{loader}.gradle.kts` as its build script.
         */
        fun match(version: String, vararg loaders: String) {
            for (loader in loaders) version("$version-$loader", version).buildscript("build.$loader.gradle.kts")
        }

        match("26.1.1", "neoforge", "fabric")

        vcsVersion = "26.1.1-neoforge"
    }
}

rootProject.name = "Construction-Wands-Revived"

// Nodes live in `loaders/{loader}`: Stonecutter looks for the shared sources at `../../src` relative to every node.
for (node in rootProject.children) {
    val loader = node.name.substringAfter('-')
    file("versions/${node.name}").delete() // the empty default folder Stonecutter created
    node.projectDir = file("loaders/$loader").also { it.mkdirs() }
    // Stonecutter points the node at its build script relative to the default directory depth
    node.buildFileName = "../../build.$loader.gradle.kts"
}
file("versions").delete() // only removed while empty

// Shortcuts: `./gradlew runNeo 1.21.1`, `runFabric 1.21.1`, `runNeoServer 1.21.1`, `runFabricServer 1.21.1`.
// The version is a plain argument, so it is rewritten into the matching node's run task before Gradle parses it.
run {
    val shortcuts = mapOf(
        "runNeo" to ("neoforge" to "runClient"),
        "runNeoServer" to ("neoforge" to "runServer"),
        "runFabric" to ("fabric" to "runClient"),
        "runFabricServer" to ("fabric" to "runServer"),
    )
    val tasks = gradle.startParameter.taskNames
    val rewritten = mutableListOf<String>()
    var i = 0
    while (i < tasks.size) {
        val shortcut = shortcuts[tasks[i]]
        val version = tasks.getOrNull(i + 1)
        if (shortcut != null && version != null && Regex("""\d+(\.\d+)+""").matches(version)) {
            rewritten += ":$version-${shortcut.first}:${shortcut.second}"
            i += 2
        } else {
            rewritten += tasks[i]
            i++
        }
    }
    if (rewritten != tasks) gradle.startParameter.setTaskNames(rewritten)
}
