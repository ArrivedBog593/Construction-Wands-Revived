package nadiendev.constructionwand;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import nadiendev.constructionwand.basics.ModStats;
import nadiendev.constructionwand.client.RenderBlockPreview;
import nadiendev.constructionwand.containers.ContainerManager;
import nadiendev.constructionwand.containers.handlers.HandlerBundle;
import nadiendev.constructionwand.containers.handlers.HandlerCapability;
import nadiendev.constructionwand.containers.handlers.HandlerShulkerbox;
import nadiendev.constructionwand.wand.undo.UndoHistory;

/**
 * Loader independent mod state. Each loader module creates the instance through {@link #init()} from its entry
 * point and then wires registries, events, networking and config to the hooks exposed by the common code.
 */
public class ConstructionWand
{
    public static final String MODID = "constructionwand";
    public static final String MODNAME = "ConstructionWand";

    public static ConstructionWand instance;
    public static final Logger LOGGER = LogManager.getLogger();

    public ContainerManager containerManager;
    public UndoHistory undoHistory;
    public RenderBlockPreview renderBlockPreview;

    private ConstructionWand() {
        containerManager = new ContainerManager();
        undoHistory = new UndoHistory();
    }

    /** Creates the shared mod instance. Must be called once, first, from every loader entry point. */
    public static ConstructionWand init() {
        if(instance == null) instance = new ConstructionWand();
        return instance;
    }

    /** Called by the loaders once registries are filled, on the common setup phase. */
    public static void commonSetup() {
        LOGGER.info("ConstructionWand says hello - may the odds be ever in your favor.");

        ModStats.init();

        // Container handlers that only need vanilla or loader neutral APIs
        instance.containerManager.register(new HandlerCapability());
        instance.containerManager.register(new HandlerShulkerbox());
        instance.containerManager.register(new HandlerBundle());
    }

    public static ResourceLocation loc(String name) {
        return new ResourceLocation(MODID, name);
    }
}
