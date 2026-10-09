package nadiendev.constructionwand.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nadiendev.constructionwand.items.containeritems.ItemVoidSack;
import nadiendev.constructionwand.wand.action.ActionDestruction;

public final class VoidSackDropHandler
{
    /**
     * Called by the loaders when an entity is about to join a level.
     *
     * @return true when the entity must be cancelled because the void sack absorbed it completely
     */
    public static boolean onEntityJoinLevel(Level eventLevel, Entity entity) {
        if (eventLevel.isClientSide()) return false;
        if (!(entity instanceof ItemEntity itemEntity)) return false;

        ActionDestruction.VoidSackDropContext ctx = ActionDestruction.ACTIVE_CONTEXT.get();
        if (ctx == null) return false;

        ServerLevel level = ctx.level();
        ItemStack sack = ctx.sack();

        if (sack.isEmpty() || !(sack.getItem() instanceof ItemVoidSack)) return false;

        ItemStack drop = itemEntity.getItem().copy();
        if (drop.isEmpty()) return false;

        int remaining = ItemVoidSack.receive(level, sack, drop);

        if (remaining <= 0) {
            // The sack absorbed everything: cancel the entity
            return true;
        } else if (remaining < drop.getCount()) {
            // The sack absorbed part of it: shrink the stack that drops
            itemEntity.getItem().setCount(remaining);
        }
        return false;
    }
}
