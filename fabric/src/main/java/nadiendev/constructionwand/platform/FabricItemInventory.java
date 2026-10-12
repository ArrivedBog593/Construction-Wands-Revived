//? if fabric {
package nadiendev.constructionwand.platform;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Exposes a Fabric Transfer API item storage as a slot based inventory. Each storage view is one slot.
 * Fabric storages are not slot addressed for insertion, so the slot of an insert is ignored.
 */
public record FabricItemInventory(Storage<ItemVariant> storage, @org.jetbrains.annotations.Nullable Runnable onCommit)
        implements IItemInventory {

    public FabricItemInventory(Storage<ItemVariant> storage) {
        this(storage, null);
    }

    private List<StorageView<ItemVariant>> views() {
        List<StorageView<ItemVariant>> views = new ArrayList<>();
        for (StorageView<ItemVariant> view : storage) {
            views.add(view);
        }
        return views;
    }

    @Override
    public int getSlots() {
        return views().size();
    }

    @Override
    public ItemStack getStack(int slot) {
        List<StorageView<ItemVariant>> views = views();
        if (slot < 0 || slot >= views.size()) return ItemStack.EMPTY;
        StorageView<ItemVariant> view = views.get(slot);
        if (view.isResourceBlank()) return ItemStack.EMPTY;
        return view.getResource().toStack((int) Math.min(view.getAmount(), Integer.MAX_VALUE));
    }

    @Override
    public ItemStack extract(int slot, int amount, boolean simulate) {
        List<StorageView<ItemVariant>> views = views();
        if (slot < 0 || slot >= views.size()) return ItemStack.EMPTY;
        StorageView<ItemVariant> view = views.get(slot);
        if (view.isResourceBlank()) return ItemStack.EMPTY;

        ItemVariant resource = view.getResource();
        try (Transaction transaction = Transaction.openOuter()) {
            long extracted = view.extract(resource, amount, transaction);
            if (!simulate) {
                transaction.commit();
                if (onCommit != null) onCommit.run();
            }
            return extracted <= 0 ? ItemStack.EMPTY : resource.toStack((int) extracted);
        }
    }

    @Override
    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        try (Transaction transaction = Transaction.openOuter()) {
            long inserted = storage.insert(ItemVariant.of(stack), stack.getCount(), transaction);
            if (!simulate) {
                transaction.commit();
                if (onCommit != null) onCommit.run();
            }
            return stack.copyWithCount(stack.getCount() - (int) inserted);
        }
    }
}
//?}
