package nadiendev.constructionwand.items.wand;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import nadiendev.constructionwand.basics.ConfigServer;
import nadiendev.constructionwand.wand.WandEnergy;

import javax.annotation.Nonnull;

public class ItemWandBasic extends ItemWand
{
    private final Tier tier;

    public ItemWandBasic(Properties properties, Tier tier) {
        super(properties.durability(tier.getUses()));
        this.tier = tier;
    }

    /** Forge calls this through its item extension; on Fabric a mixin on ItemStack redirects to it. */
    public int getMaxDamage(ItemStack stack) {
        return ConfigServer.getWandProperties(this).getDurability();
    }

    @Override
    public int getEnchantmentValue() {
        return tier.getEnchantmentValue();
    }

    @Override
    public int remainingDurability(ItemStack stack) {
        if(WandEnergy.isInstalled(stack)) return WandEnergy.blocksLeft(stack);
        return stack.getMaxDamage() - stack.getDamageValue();
    }

    // With the Energy Upgrade the item bar shows the stored energy instead of the durability
    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return WandEnergy.isInstalled(stack) || super.isBarVisible(stack);
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        if(!WandEnergy.isInstalled(stack)) return super.getBarWidth(stack);
        return Math.round(13.0F * WandEnergy.get(stack) / WandEnergy.capacity());
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        return WandEnergy.isInstalled(stack) ? 0xE8C020 : super.getBarColor(stack);
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return this.tier.getRepairIngredient().test(repair);
    }
}
