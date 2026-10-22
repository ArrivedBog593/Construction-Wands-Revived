package nadiendev.constructionwand.wand;

import net.minecraft.world.item.ItemStack;
import nadiendev.constructionwand.basics.ConfigServer;
import nadiendev.constructionwand.component.ModDataComponents;
import nadiendev.constructionwand.items.wand.ItemWandBasic;

/**
 * The energy of a wand with the Energy Upgrade. It lives in the stack, and the loaders expose it to the energy
 * networks of their platform (FE on NeoForge and Forge, Team Reborn Energy on Fabric).
 */
public final class WandEnergy
{
    private WandEnergy() {}

    public static boolean isInstalled(ItemStack wand) {
        return wand.getItem() instanceof ItemWandBasic && wand.getOrDefault(ModDataComponents.ENERGY_UPGRADE.get(), false);
    }

    public static int capacity() {
        return ConfigServer.ENERGY_CAPACITY.get();
    }

    public static int costPerBlock() {
        return ConfigServer.ENERGY_PER_BLOCK.get();
    }

    public static int get(ItemStack wand) {
        return Math.max(0, Math.min(capacity(), wand.getOrDefault(ModDataComponents.ENERGY.get(), 0)));
    }

    private static void set(ItemStack wand, int energy) {
        wand.set(ModDataComponents.ENERGY.get(), Math.max(0, Math.min(capacity(), energy)));
    }

    /** Stores up to {@code amount}, returns what was accepted. */
    public static int receive(ItemStack wand, int amount, boolean simulate) {
        if(!isInstalled(wand) || amount <= 0) return 0;
        int accepted = Math.min(capacity() - get(wand), amount);
        if(accepted > 0 && !simulate) set(wand, get(wand) + accepted);
        return accepted;
    }

    /** Takes up to {@code amount}, returns what was taken. */
    public static int extract(ItemStack wand, int amount, boolean simulate) {
        if(!isInstalled(wand) || amount <= 0) return 0;
        int taken = Math.min(get(wand), amount);
        if(taken > 0 && !simulate) set(wand, get(wand) - taken);
        return taken;
    }

    /** How many blocks the stored energy pays for. */
    public static int blocksLeft(ItemStack wand) {
        int cost = costPerBlock();
        return cost <= 0 ? Integer.MAX_VALUE : get(wand) / cost;
    }

    /** Pays for one block. */
    public static boolean spendBlock(ItemStack wand) {
        return extract(wand, costPerBlock(), false) >= costPerBlock();
    }
}
