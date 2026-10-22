package nadiendev.constructionwand.containers;

import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.containers.handlers.HandlerBotania;
import nadiendev.constructionwand.containers.handlers.HandlerSophisticatedBackpack;
import net.neoforged.fml.ModList;

/** Registers the container handlers of optional NeoForge-only mods. Vanilla and generic ones live in common. */
public class ContainerRegistrar
{
    public static void register() {
        registerBotania();

        registerSophisticatedBackpacks();
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


}
