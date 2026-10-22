package nadiendev.constructionwand.energy;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.DelegatingEnergyStorage;
import nadiendev.constructionwand.wand.WandEnergy;

/**
 * The energy of a wand with the Energy Upgrade as a Team Reborn Energy item storage: chargers and cables fill it,
 * nothing drains it. The energy lives in the stack ({@link WandEnergy}), and like the item storages of Team Reborn
 * Energy it is changed by swapping the stack of the {@link ContainerItemContext} for a copy with the new energy inside
 * a nested transaction, so an aborted transaction rolls the stack back by itself.
 */
public class WandEnergyStorage implements EnergyStorage
{
    private final ContainerItemContext ctx;

    private WandEnergyStorage(ContainerItemContext ctx) {
        this.ctx = ctx;
    }

    /** The storage for the stack of the context, null when it has no Energy Upgrade. */
    public static EnergyStorage find(ItemStack stack, ContainerItemContext ctx) {
        if(!WandEnergy.isInstalled(stack)) return null;

        Item startingItem = ctx.getItemVariant().getItem();
        // Stops working as soon as the context no longer holds the same wand (moved, replaced or gone)
        return new DelegatingEnergyStorage(new WandEnergyStorage(ctx),
                () -> ctx.getItemVariant().isOf(startingItem) && ctx.getAmount() > 0);
    }

    /** The energy of one wand of the context. */
    private long energyPerCount() {
        return WandEnergy.get(ctx.getItemVariant().toStack());
    }

    /** Swaps {@code count} wands for the same wands holding {@code energy}; false when the context refuses. */
    private boolean trySetEnergy(int energy, long count, TransactionContext transaction) {
        ItemStack newStack = ctx.getItemVariant().toStack();
        // Goes through the same code as any other change of the energy, so the capacity and the upgrade are checked
        WandEnergy.receive(newStack, energy - WandEnergy.get(newStack), false);
        ItemVariant newVariant = ItemVariant.of(newStack);

        try(Transaction nested = transaction.openNested()) {
            if(ctx.extract(ctx.getItemVariant(), count, nested) == count && ctx.insert(newVariant, count, nested) == count) {
                nested.commit();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean supportsInsertion() {
        return true;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        long count = ctx.getAmount();
        if(count <= 0) return 0;

        long current = energyPerCount();
        long insertedPerCount = Math.min(maxAmount / count, WandEnergy.capacity() - current);

        if(insertedPerCount > 0 && trySetEnergy((int) (current + insertedPerCount), count, transaction)) {
            return insertedPerCount * count;
        }
        return 0;
    }

    @Override
    public boolean supportsExtraction() {
        return false;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long getAmount() {
        return ctx.getAmount() * energyPerCount();
    }

    @Override
    public long getCapacity() {
        return ctx.getAmount() * WandEnergy.capacity();
    }
}
