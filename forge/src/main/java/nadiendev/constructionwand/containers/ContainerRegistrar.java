package nadiendev.constructionwand.containers;

import net.minecraftforge.fml.ModList;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.containers.handlers.HandlerBotania;

/** Registers the container handlers that need APIs of optional mods. */
public class ContainerRegistrar
{
    public static void register() {
        if(ModList.get().isLoaded("botania")) {
            ConstructionWand.instance.containerManager.register(new HandlerBotania());
            ConstructionWand.LOGGER.info("Botania integration added");
        }
    }
}
