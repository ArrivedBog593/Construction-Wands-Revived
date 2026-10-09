package nadiendev.constructionwand.basics;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import nadiendev.constructionwand.registry.Registrar;
import nadiendev.constructionwand.registry.RegistryObject;
import nadiendev.constructionwand.ConstructionWand;

import java.util.function.Supplier;

public class ModStats
{
    public static final Registrar<ResourceLocation> CUSTOM_STATS = new Registrar<>(BuiltInRegistries.CUSTOM_STAT);

    public static final ResourceLocation USE_WAND = ResourceLocation.fromNamespaceAndPath(ConstructionWand.MODID, "use_wand");
    public static final Supplier<ResourceLocation> USE_WAND_STAT = CUSTOM_STATS.register("use_wand", () -> USE_WAND);

    public static void init() {
    }
}
