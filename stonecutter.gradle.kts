plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.1-forge"

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
    }

    replacements {
        // ResourceLocation was renamed in 1.21.11
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }
    }
}
