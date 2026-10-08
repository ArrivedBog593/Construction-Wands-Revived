//? if forge {
/*package nadiendev.constructionwand.platform;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/^* Exposes a Forge {@link IItemHandler} as a common inventory. ^/
public record ForgeItemInventory(IItemHandler handler) implements IItemInventory {

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Override
    public ItemStack getStack(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    public ItemStack extract(int slot, int amount, boolean simulate) {
        return handler.extractItem(slot, amount, simulate);
    }

    @Override
    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        return handler.insertItem(slot, stack, simulate);
    }
}
*///?}
