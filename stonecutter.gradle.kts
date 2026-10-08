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
        match(loader, "neoforge", "fabric")

        // Version thresholds that need to be combined with loader constants
        val clientItems = current.parsed >= "1.21.4"
        this["client_items"] = clientItems
        this["neo_client_items"] = clientItems && loader == "neoforge"
        this["neo_legacy_items"] = !clientItems && loader == "neoforge"
        this["fabric_client_items"] = clientItems && loader == "fabric"
        this["fabric_outline_mixin"] = current.parsed >= "1.21.9" && loader == "fabric"

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

        // TriState moved into vanilla in 1.21.5
        string(current.parsed >= "1.21.5") {
            replace("net.neoforged.neoforge.common.util.TriState", "net.minecraft.util.TriState")
        }

        // ResourceLocation was renamed in 1.21.11
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }
    }
}
