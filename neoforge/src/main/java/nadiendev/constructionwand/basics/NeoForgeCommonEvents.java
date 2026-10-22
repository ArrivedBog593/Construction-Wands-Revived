package nadiendev.constructionwand.basics;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.events.VoidSackDropHandler;
import nadiendev.constructionwand.events.VoidSackPickupHandler;

/** Forwards NeoForge's void sack related game events to the shared handlers. */
@EventBusSubscriber(modid = ConstructionWand.MODID)
public final class NeoForgeCommonEvents {

    private NeoForgeCommonEvents() {}

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (VoidSackDropHandler.onEntityJoinLevel(event.getLevel(), event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Pre event) {
        if (VoidSackPickupHandler.onItemPickup(event.getPlayer(), event.getItemEntity())) {
            event.setCanPickup(TriState.FALSE);
        }
    }
}
