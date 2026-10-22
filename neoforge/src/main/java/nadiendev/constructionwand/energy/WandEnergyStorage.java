package nadiendev.constructionwand.energy;

import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import nadiendev.constructionwand.component.ModDataComponents;
import nadiendev.constructionwand.wand.WandEnergy;

/** The energy of a wand with the Energy Upgrade as an FE item handler: chargers fill it, nothing drains it. */
public class WandEnergyStorage extends ItemAccessEnergyHandler
{
    public WandEnergyStorage(ItemAccess access) {
        super(access, ModDataComponents.ENERGY.get(), WandEnergy.capacity(), WandEnergy.capacity(), 0);
    }
}
