package nadiendev.constructionwand.items.wand;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nadiendev.constructionwand.basics.ConfigServer;
import nadiendev.constructionwand.basics.option.WandOptions;
import nadiendev.constructionwand.items.ModItems;
import nadiendev.constructionwand.items.core.ItemCoreExchange;
import nadiendev.constructionwand.wand.WandEnergy;

import javax.annotation.Nonnull;

public class ItemWandBasic extends ItemWand
{
    private final WandTier tier;

    public ItemWandBasic(Properties properties, WandTier tier) {
        super(tier.applyTo(properties));
        this.tier = tier;
    }

    /**
     * Per-stack max durability. NeoForge and Forge route {@code ItemStack#getMaxDamage} through this method;
     * {@link #inventoryTick} mirrors the value into the MAX_DAMAGE component for loaders without such a hook.
     */
    public int getMaxDamage(ItemStack stack) {
        // Special case: Gold Construction Wand + Exchange Core must have exactly
        // half the durability of the Diamond Construction Wand + Exchange Core.
        // Diamond's own durability isn't affected by which core is installed, so this
        // simply reads Diamond's configured durability and halves it, staying in sync
        // if the server admin changes Diamond's config value.
        if(this.tier == WandTier.GOLD && new WandOptions(stack).cores.get() instanceof ItemCoreExchange) {
            int diamondDurability = ConfigServer.getWandProperties(ModItems.WAND_DIAMOND.get()).getDurability();
            return diamondDurability / 2;
        }
        return ConfigServer.getWandProperties(this).getDurability();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if(level.isClientSide()) return;
        syncMaxDamage(stack);
    }

    private void syncMaxDamage(ItemStack stack) {
        int max = getMaxDamage(stack);
        if(max > 0 && !Integer.valueOf(max).equals(stack.get(DataComponents.MAX_DAMAGE))) {
            stack.set(DataComponents.MAX_DAMAGE, max);
        }
    }

    @Override
    public int getEnchantmentValue() {
        return tier.enchantability();
    }

    @Override
    public int remainingDurability(ItemStack stack) {
        if(WandEnergy.isInstalled(stack)) return WandEnergy.blocksLeft(stack);
        return stack.getMaxDamage() - stack.getDamageValue();
    }

    // With the Energy Upgrade the item bar shows the stored energy instead of the durability
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return WandEnergy.isInstalled(stack) || super.isBarVisible(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if(!WandEnergy.isInstalled(stack)) return super.getBarWidth(stack);
        return Math.round(13.0F * WandEnergy.get(stack) / WandEnergy.capacity());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return WandEnergy.isInstalled(stack) ? 0xE8C020 : super.getBarColor(stack);
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return this.tier.isRepairItem(repair);
    }
}