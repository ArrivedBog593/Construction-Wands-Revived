plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.10-neoforge"

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

        // Package and method renames of 1.21.11
        string(current.parsed >= "26.2") {
            replace("net.minecraft.advancements.critereon.InventoryChangeTrigger", "net.minecraft.advancements.triggers.InventoryChangeTrigger")
            replace("net.minecraft.advancements.critereon.ItemPredicate", "net.minecraft.advancements.predicates.ItemPredicate")
            replace("Minecraft.getInstance().setScreen(", "Minecraft.getInstance().gui.setScreen(")
            replace("minecraft.screen == null", "minecraft.gui.screen() == null")
            replace("gameRenderer.getMainCamera()", "gameRenderer.mainCamera()")
        }

        string(current.parsed >= "1.21.11" && current.parsed < "26.2") {
            replace("net.minecraft.advancements.critereon", "net.minecraft.advancements.criterion")
        }

        string(current.parsed >= "1.21.11") {
            replace("net.minecraft.client.renderer.RenderType", "net.minecraft.client.renderer.rendertype.RenderType")
            replace(".dimension().location()", ".dimension().identifier()")
        }

        // Fabric API renames of 26.1
        string(current.parsed >= "26.1") {
            replace("keybinding.v1", "keymapping.v1")
            replace("KeyBindingHelper", "KeyMappingHelper")
            replace("registerKeyBinding", "registerKeyMapping")
            replace("itemgroup.v1", "creativetab.v1")
            replace("FabricItemGroup", "FabricCreativeModeTab")
            replace("screenhandler.v1", "menu.v1")
            replace("ExtendedScreenHandlerType", "ExtendedMenuType")
            replace("ExtendedScreenHandlerFactory", "ExtendedMenuProvider")
            replace(".playS2C()", ".clientboundPlay()")
            replace(".playC2S()", ".serverboundPlay()")
            replace("net.minecraft.client.renderer.state.LevelRenderState", "net.minecraft.client.renderer.state.level.LevelRenderState")
        }

        // Keyboard input switched to SDL in 26.3
        string(current.parsed >= "26.3") {
            replace("InputConstants.Type.KEYSYM", "InputConstants.Type.KEYBOARD")
        }

        // GuiGraphics became GuiGraphicsExtractor in 26.1
        string(current.parsed >= "26.1") {
            replace("GuiGraphics", "GuiGraphicsExtractor")
        }

        // ResourceLocation was renamed in 1.21.11
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }
    }
}
