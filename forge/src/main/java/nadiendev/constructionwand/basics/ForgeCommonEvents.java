package nadiendev.constructionwand.basics;

import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.events.VoidSackDropHandler;
import nadiendev.constructionwand.events.VoidSackPickupHandler;

/** Forwards Forge's void sack related game events to the shared handlers. */
@Mod.EventBusSubscriber(modid = ConstructionWand.MODID)
public final class ForgeCommonEvents
{
    private ForgeCommonEvents() {}

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if(VoidSackDropHandler.onEntityJoinLevel(event.getLevel(), event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        if(VoidSackPickupHandler.onItemPickup(event.getEntity(), event.getItem())) {
            event.setCanceled(true);
        }
    }
}
