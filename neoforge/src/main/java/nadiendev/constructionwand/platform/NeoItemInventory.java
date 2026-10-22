package nadiendev.constructionwand.platform;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * Exposes a NeoForge item {@link ResourceHandler} as a common inventory.
 *
 * @param onCommit run after a change was committed, used to copy the result back into the real item stack
 */
public record NeoItemInventory(ResourceHandler<ItemResource> handler, @org.jetbrains.annotations.Nullable Runnable onCommit)
        implements IItemInventory {

    public NeoItemInventory(ResourceHandler<ItemResource> handler) {
        this(handler, null);
    }

    @Override
    public int getSlots() {
        return handler.size();
    }

    @Override
    public ItemStack getStack(int slot) {
        return handler.getResource(slot).toStack(handler.getAmountAsInt(slot));
    }

    @Override
    public ItemStack extract(int slot, int amount, boolean simulate) {
        ItemResource resource = handler.getResource(slot);
        if (resource.isEmpty() || amount <= 0) return ItemStack.EMPTY;
        try (Transaction transaction = Transaction.open(null)) {
            int extracted = handler.extract(slot, resource, amount, transaction);
            if (extracted <= 0) return ItemStack.EMPTY;
            if (!simulate) {
                transaction.commit();
                if (onCommit != null) onCommit.run();
            }
            return resource.toStack(extracted);
        }
    }

    @Override
    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return stack;
        try (Transaction transaction = Transaction.open(null)) {
            int inserted = handler.insert(slot, ItemResource.of(stack), stack.getCount(), transaction);
            if (inserted <= 0) return stack;
            if (!simulate) {
                transaction.commit();
                if (onCommit != null) onCommit.run();
            }
            return stack.copyWithCount(stack.getCount() - inserted);
        }
    }
}
