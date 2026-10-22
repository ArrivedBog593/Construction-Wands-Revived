package nadiendev.constructionwand.client.property;

import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.basics.option.WandOptions;
import nadiendev.constructionwand.items.ModItems;
import nadiendev.constructionwand.items.core.ItemCoreAngel;
import nadiendev.constructionwand.items.core.ItemCoreDestruction;
import nadiendev.constructionwand.items.core.ItemCoreExchange;
import nadiendev.constructionwand.items.wand.ItemWand;
import nadiendev.constructionwand.platform.Services;
import nadiendev.constructionwand.registry.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/** Tells the wand item models which core is installed, which the model overrides use to pick the overlay of that core. */
public class SelectWandCore
{
    public static final ResourceLocation WAND_CORE = ConstructionWand.loc("wand_core");

    /** Registers the wand core predicate. ItemProperties#register is not thread-safe, so run it on the main thread. */
    public static void register() {
        for(RegistryObject<Item> wand : ModItems.WANDS) {
            Services.PLATFORM.registerItemProperty(wand.get(), WAND_CORE, (stack, level, entity, seed) -> {
                if(!(stack.getItem() instanceof ItemWand)) return 0f;
                var core = new WandOptions(stack).cores.get();
                if(core instanceof ItemCoreAngel) return 1f;       // core_angel active
                if(core instanceof ItemCoreDestruction) return 2f; // core_destruction active
                if(core instanceof ItemCoreExchange) return 3f;    // core_exchange active
                return 0f;                                         // default (no core)
            });
        }
    }
}
