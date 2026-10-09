//? if neoforge && sophisticatedbackpacks {
/*package nadiendev.constructionwand.containers.handlers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nadiendev.constructionwand.api.IContainerHandler;
import nadiendev.constructionwand.basics.WandUtil;
import nadiendev.constructionwand.containers.ContainerTrace;
import nadiendev.constructionwand.platform.IItemInventory;
import nadiendev.constructionwand.platform.Services;

public class HandlerSophisticatedBackpack implements IContainerHandler {

    @Override
    public boolean matches(Player player, ItemStack itemStack, ItemStack inventoryStack) {
        if (inventoryStack == null || inventoryStack.isEmpty() || inventoryStack.getCount() != 1) {
            return false;
        }
        String namespace = BuiltInRegistries.ITEM
                .getKey(inventoryStack.getItem())
                .getNamespace();
        if (!"sophisticatedbackpacks".equals(namespace)) {
            return false;
        }
        return Services.PLATFORM.getItemInventory(inventoryStack) != null;
    }

    @Override
    public int countItems(Player player, ContainerTrace trace, ItemStack itemStack, ItemStack inventoryStack) {
        IItemInventory handler = Services.PLATFORM.getItemInventory(inventoryStack);
        if (handler == null) return 0;

        int count = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack slotStack = handler.getStack(i);
            if (WandUtil.stackEquals(slotStack, itemStack)) {
                count += slotStack.getCount();
            }
        }
        return count;
    }

    @Override
    public int useItems(Player player, ContainerTrace trace, ItemStack itemStack, ItemStack inventoryStack, int count) {
        IItemInventory handler = Services.PLATFORM.getItemInventory(inventoryStack);
        if (handler == null) return count;

        boolean changed = false;

        for (int i = 0; i < handler.getSlots() && count > 0; i++) {
            ItemStack slotStack = handler.getStack(i);
            if (!WandUtil.stackEquals(slotStack, itemStack)) continue;

            int toTake = Math.min(count, slotStack.getCount());
            ItemStack extracted = handler.extract(i, toTake, false);
            if (!extracted.isEmpty()) {
                count -= extracted.getCount();
                changed = true;
            }
        }

        if (changed) {
            player.getInventory().setChanged();
        }

        return count;
    }
}
*///?}
