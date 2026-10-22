package nadiendev.constructionwand.items.upgrade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.api.IWandUpgrade;

/**
 * Installed in a wand like a core, but not selectable: the wand then runs on energy instead of durability.
 */
public class ItemEnergyUpgrade extends Item implements IWandUpgrade
{
    public ItemEnergyUpgrade(Properties properties) {
        super(properties);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ConstructionWand.loc("energy_upgrade");
    }
}
