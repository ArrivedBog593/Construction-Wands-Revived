package nadiendev.constructionwand.platform;

import net.minecraft.world.item.ItemStack;

/** Minimal slot based view over a loader specific item handler / storage. */
public interface IItemInventory {
    int getSlots();

    ItemStack getStack(int slot);

    /** Extracts up to {@code amount} items from the slot and returns what was extracted. */
    ItemStack extract(int slot, int amount, boolean simulate);

    /** Tries to insert the stack, starting at the given slot, and returns the remainder. */
    ItemStack insert(int slot, ItemStack stack, boolean simulate);

    /** Tries to insert the stack into any slot and returns the remainder. */
    default ItemStack insertAnywhere(ItemStack stack) {
        ItemStack remaining = stack;
        for (int i = 0; i < getSlots() && !remaining.isEmpty(); i++) {
            remaining = insert(i, remaining, false);
        }
        return remaining;
    }
}
