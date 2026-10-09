package nadiendev.constructionwand.client.property;

import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.basics.option.WandOptions;
import nadiendev.constructionwand.items.ModItems;
import nadiendev.constructionwand.items.wand.ItemWand;
import nadiendev.constructionwand.platform.Services;
import nadiendev.constructionwand.registry.RegistryObject;
import net.minecraft.world.item.Item;

/** Tells the wand item models whether a core is installed, which the model overrides use to pick the overlay. */
public class SelectWandCore
{
    public static void register() {
        for(RegistryObject<Item> itemSupplier : ModItems.WANDS) {
            Item item = itemSupplier.get();
            Services.PLATFORM.registerItemProperty(item, ConstructionWand.loc("using_core"),
                    (stack, world, entity, n) -> entity == null || !(stack.getItem() instanceof ItemWand) ? 0 :
                            new WandOptions(stack).cores.get().getColor() > -1 ? 1 : 0);
        }
    }
}
