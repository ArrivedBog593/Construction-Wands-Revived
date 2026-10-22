package nadiendev.constructionwand.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import nadiendev.constructionwand.wand.WandEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Attached to every basic wand stack (see {@code ForgeCommonEvents}). The FE capability only exists once the Energy
 * Upgrade is installed, which is checked on every request because the upgrade is added to the stack later.
 */
public class WandEnergyProvider implements ICapabilityProvider
{
    private final ItemStack wand;
    private final LazyOptional<WandEnergyStorage> storage;

    public WandEnergyProvider(ItemStack wand) {
        this.wand = wand;
        this.storage = LazyOptional.of(() -> new WandEnergyStorage(wand));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY && WandEnergy.isInstalled(wand)) return storage.cast();
        return LazyOptional.empty();
    }
}
