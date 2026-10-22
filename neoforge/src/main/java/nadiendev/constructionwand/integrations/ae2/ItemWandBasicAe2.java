package nadiendev.constructionwand.integrations.ae2;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerUnit;
import appeng.api.implementations.items.IAEItemPowerStorage;
import net.minecraft.world.item.ItemStack;
import nadiendev.constructionwand.items.wand.ItemWandBasic;
import nadiendev.constructionwand.items.wand.WandTier;
import nadiendev.constructionwand.wand.WandEnergy;

/**
 * The basic wand when Applied Energistics 2 is installed: with the Energy Upgrade it can be charged in the
 * AE2 Charger, which only knows items that implement {@link IAEItemPowerStorage}.
 */
public class ItemWandBasicAe2 extends ItemWandBasic implements IAEItemPowerStorage
{
    public ItemWandBasicAe2(Properties properties, WandTier tier) {
        super(properties, tier);
    }

    @Override
    public double injectAEPower(ItemStack stack, double amount, Actionable mode) {
        double required = getAEMaxPower(stack) - getAECurrentPower(stack);
        double overflow = Math.max(0.0, Math.min(amount - required, amount));
        if(mode == Actionable.MODULATE) {
            double toAdd = Math.min(amount, required);
            WandEnergy.receive(stack, (int) Math.floor(PowerUnit.AE.convertTo(PowerUnit.FE, toAdd)), false);
        }
        return overflow;
    }

    @Override
    public double extractAEPower(ItemStack stack, double amount, Actionable mode) {
        return 0;
    }

    @Override
    public double getAEMaxPower(ItemStack stack) {
        return WandEnergy.isInstalled(stack) ? PowerUnit.FE.convertTo(PowerUnit.AE, WandEnergy.capacity()) : 0;
    }

    @Override
    public double getAECurrentPower(ItemStack stack) {
        return WandEnergy.isInstalled(stack) ? PowerUnit.FE.convertTo(PowerUnit.AE, WandEnergy.get(stack)) : 0;
    }

    @Override
    public AccessRestriction getPowerFlow(ItemStack stack) {
        // Read only (not chargeable) until the Energy Upgrade is installed
        return WandEnergy.isInstalled(stack) ? AccessRestriction.WRITE : AccessRestriction.READ;
    }

    @Override
    public double getChargeRate(ItemStack stack) {
        return 800d;
    }
}
