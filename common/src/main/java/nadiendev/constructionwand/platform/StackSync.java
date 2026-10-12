package nadiendev.constructionwand.platform;

import net.minecraft.world.item.ItemStack;

/**
 * The loader transfer APIs modify the stack they are handed by swapping it for a copy inside a throwaway container.
 * Container items (backpacks and the like) are therefore worked on as a copy, and this writes the result back into the
 * stack that actually sits in the player's inventory.
 */
public final class StackSync
{
    private StackSync() {}

    /** Makes {@code target} (count and tag) match {@code source}. The item is assumed to be the same. */
    public static void copyInto(ItemStack target, ItemStack source) {
        if(target == source) return;
        target.setTag(source.getTag() == null ? null : source.getTag().copy());
        target.setCount(source.getCount());
    }
}
