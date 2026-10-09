package nadiendev.constructionwand;

import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import nadiendev.constructionwand.client.RenderBlockPreview;
import nadiendev.constructionwand.containers.ContainerManager;
import nadiendev.constructionwand.wand.undo.UndoHistory;

/**
 * Loader independent mod state. Each loader module creates the instance through {@link #init()} from its entry
 * point and then wires registries, events, networking and config to the hooks exposed by the common code.
 */
public class ConstructionWand {
    public static final String MODID = "constructionwand";
    public static final String MODNAME = "ConstructionWandRevived";

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
        if (instance == null) instance = new ConstructionWand();
        return instance;
    }

    /** Called by the loaders once registries are filled, on the common setup phase. */
    public static void commonSetup() {
        LOGGER.info("ConstructionWand says hello - may the odds be ever in your favor.");
        instance.containerManager.registerDefaults();
    }

    public static Identifier loc(String name) {
        return Identifier.fromNamespaceAndPath(MODID, name);
    }
}
