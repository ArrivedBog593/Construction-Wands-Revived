package nadiendev.constructionwand.integrations.ae2;

import net.minecraft.world.item.Item;
import nadiendev.constructionwand.items.wand.WandTier;

/**
 * Creates the AE2 variant of the basic wand. It lives in its own class so that the JVM only loads the AE2 wand (and
 * the AE2 interface it implements) when AE2 is installed: verifying the platform helper would otherwise already
 * load it, and the game would crash on startup without AE2.
 */
public final class Ae2Wands
{
    private Ae2Wands() {}

    public static Item create(Item.Properties properties, WandTier tier) {
        return new ItemWandBasicAe2(properties, tier);
    }
}
