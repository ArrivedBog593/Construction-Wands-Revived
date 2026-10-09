//? if neoforge {
package nadiendev.constructionwand.containers;

import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.containers.handlers.HandlerBotania;
//? if sophisticatedbackpacks
import nadiendev.constructionwand.containers.handlers.HandlerSophisticatedBackpack;
//? if ae2 {
import nadiendev.constructionwand.containers.handlers.HandlerPortableCell;
import nadiendev.constructionwand.containers.handlers.HandlerWirelessTerminal;
//?}
//? if refinedstorage
//import nadiendev.constructionwand.containers.handlers.HandlerWirelessGrid;
//? if ae2wtlib
import nadiendev.constructionwand.containers.handlers.HandlerWirelessTerminalAE2WTLib;
import net.neoforged.fml.ModList;

/** Registers the container handlers of optional NeoForge-only mods. Vanilla and generic ones live in common. */
public class ContainerRegistrar
{
    public static void register() {
        registerBotania();

        //? if sophisticatedbackpacks
        registerSophisticatedBackpacks();
        //? if ae2 || ae2wtlib
        registerAppliedEnergistics();
        //? if refinedstorage
        //registerRefinedStorage();
    }

    private static void registerBotania() {
        try {
            if (ModList.get().isLoaded("botania")) {
                ConstructionWand.instance.containerManager.register(new HandlerBotania());
                ConstructionWand.LOGGER.info("Botania integration added");
            }
        } catch (Exception | LinkageError e) {
            ConstructionWand.LOGGER.warn("Could not load Botania integration: " + e);
        }
    }

    //? if sophisticatedbackpacks {
    private static void registerSophisticatedBackpacks() {
        try {
            boolean loaded = ModList.get().isLoaded("sophisticatedbackpacks");
            if (loaded) {
                ConstructionWand.instance.containerManager.register(new HandlerSophisticatedBackpack());
                ConstructionWand.LOGGER.info("Sophisticated BackPack integration added");
            }
        } catch (Exception e) {
            ConstructionWand.LOGGER.warn("Could not load Sophisticated Backpacks integration: " + e.getMessage());
        }
    }
    //?}

    //? if ae2 || ae2wtlib {
    private static void registerAppliedEnergistics() {
        //? if ae2 {
        try {
            boolean loaded = ModList.get().isLoaded("ae2");
            if (loaded) {
                ConstructionWand.instance.containerManager.register(new HandlerPortableCell());
                ConstructionWand.instance.containerManager.register(new HandlerWirelessTerminal());
                ConstructionWand.LOGGER.info("Applied Energistics 2 integration added");
            }
        } catch (Exception e) {
            ConstructionWand.LOGGER.warn("Could not load Applied Energistics 2 integration: " + e.getMessage());
        }
        //?}

        //? if ae2wtlib {
        try {
            boolean loaded = ModList.get().isLoaded("ae2wtlib");
            if (loaded) {
                ConstructionWand.instance.containerManager.register(new HandlerWirelessTerminalAE2WTLib());
                ConstructionWand.LOGGER.info("AE2 Wireless Terminals integration added");
            }
        } catch (Exception e) {
            ConstructionWand.LOGGER.warn("Could not load AE2 Wireless Terminals integration: " + e.getMessage());
        }
        //?}
    }
    //?}

    //? if refinedstorage {
    /*private static void registerRefinedStorage() {
        try {
            boolean loaded = ModList.get().isLoaded("refinedstorage");
            if (loaded) {
                ConstructionWand.instance.containerManager.register(new HandlerWirelessGrid());
                ConstructionWand.LOGGER.info("Refined Storage integration added");
            }
        } catch (Exception e) {
            ConstructionWand.LOGGER.warn("Could not load Refined Storage integration: " + e.getMessage());
        }
    }
    *///?}
}
//?}
