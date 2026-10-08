plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.1-neoforge"

// Parameters applied to every node. See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    val (version, loader) = current.project.split('-', limit = 2)

    // Makes version- and loader-specific tables of stonecutter.properties.toml apply
    properties {
        tags(version, loader)
    }

    // Loader constants for `//? if neoforge {` style comments
    constants {
        match(loader, "neoforge", "fabric", "forge")

        // Optional integrations are enabled only for versions that declare a dependency version for them
        for (dep in listOf("jei", "rei", "emi", "ae2", "refinedstorage", "ae2wtlib", "sophisticatedbackpacks", "curios")) {
            this[dep] = properties.getOrNull<String>("deps.$dep") != null
        }
    }

    replacements {
        // Direction#getNormal was renamed in 1.21.2
        string(current.parsed >= "1.21.2") {
            replace(".getNormal()", ".getUnitVec3i()")
        }

        // ResourceLocation was renamed in 1.21.11
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }
    }
}
