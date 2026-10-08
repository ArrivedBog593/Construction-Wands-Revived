package nadiendev.constructionwand.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import nadiendev.constructionwand.items.containeritems.ItemVoidSack;

public final class VoidSackPickupHandler
{
    /**
     * Called by the loaders before a player picks up an item entity.
     *
     * @return true when the pickup must be cancelled because the void sack absorbed everything
     */
    public static boolean onItemPickup(Player player, ItemEntity itemEntity) {
        if (!(player instanceof ServerPlayer sp)) return false;
        if (!(player.serverLevel() instanceof ServerLevel level)) return false;

        ItemStack sack = findActiveSack(sp);
        if (sack == null) return false;

        ItemStack picked = itemEntity.getItem().copy();
        if (picked.isEmpty()) return false;

        int originalCount = picked.getCount();
        int remaining = ItemVoidSack.interceptPickup(level, sack, picked);

        if (remaining >= originalCount) return false;

        int absorbed = originalCount - remaining;

        ItemStack entityStack = itemEntity.getItem();
        entityStack.shrink(absorbed);

        if (remaining == 0) {
            if (itemEntity.getItem().isEmpty()) {
                itemEntity.discard();
            }
            return true;
        }
        return false;
    }

    private static ItemStack findActiveSack(ServerPlayer player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof ItemVoidSack && ItemVoidSack.isActive(stack)) {
                return stack;
            }
        }
        return null;
    }
}