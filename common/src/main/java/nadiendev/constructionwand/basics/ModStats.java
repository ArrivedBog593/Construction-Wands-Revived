package nadiendev.constructionwand.basics;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.registry.Registrar;
import nadiendev.constructionwand.registry.RegistryObject;

public class ModStats
{
    public static final Registrar<ResourceLocation> CUSTOM_STATS = new Registrar<>(BuiltInRegistries.CUSTOM_STAT);

    public static final ResourceLocation USE_WAND = new ResourceLocation(ConstructionWand.MODID, "use_wand");
    public static final RegistryObject<ResourceLocation> USE_WAND_STAT = CUSTOM_STATS.register("use_wand", () -> USE_WAND);

    /** Creates the stat once its id is registered. */
    public static void init() {
        Stats.CUSTOM.get(USE_WAND, StatFormatter.DEFAULT);
    }
}
