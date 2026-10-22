package nadiendev.constructionwand.energy;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.IEnergyStorage;
import nadiendev.constructionwand.wand.WandEnergy;

/** The energy of a wand with the Energy Upgrade as an FE item storage: chargers fill it, nothing drains it. */
public class WandEnergyStorage implements IEnergyStorage
{
    private final ItemStack wand;

    public WandEnergyStorage(ItemStack wand) {
        this.wand = wand;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        return WandEnergy.receive(wand, toReceive, simulate);
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return WandEnergy.get(wand);
    }

    @Override
    public int getMaxEnergyStored() {
        return WandEnergy.capacity();
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}

