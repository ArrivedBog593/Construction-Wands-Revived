package nadiendev.constructionwand.energy;

import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import nadiendev.constructionwand.component.ModDataComponents;
import nadiendev.constructionwand.wand.WandEnergy;

/**
 * The energy of a wand with the Energy Upgrade as an item energy handler: chargers fill it, nothing drains it.
 * It is stored in the {@code energy} data component, the same one {@link WandEnergy} reads.
 */
public class WandEnergyStorage extends ItemAccessEnergyHandler
{
    public WandEnergyStorage(ItemAccess access) {
        // Capacity is the configured one, receive up to the capacity at once, extract nothing
        super(access, ModDataComponents.ENERGY.get(), WandEnergy.capacity(), WandEnergy.capacity(), 0);
    }
}
