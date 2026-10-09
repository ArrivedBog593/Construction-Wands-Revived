package nadiendev.constructionwand.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nadiendev.constructionwand.ConstructionWand;

/** Forwards Forge's client side game events to the shared client hooks. */
@Mod.EventBusSubscriber(modid = ConstructionWand.MODID, value = Dist.CLIENT)
public final class ForgeClientEvents
{
    private ForgeClientEvents() {}

    @SubscribeEvent
    public static void onKey(InputEvent.Key event) {
        ClientEvents.onKey();
        KeybindHandler.onKey();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if(ClientEvents.onMouseScroll(event.getScrollDelta())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        ClientEvents.onLeftClickEmpty(event.getEntity(), event.getItemStack());
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if(event.getSide().isServer()) return;
        if(ClientEvents.onRightClickItem(event.getEntity(), event.getItemStack())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRenderHighlight(RenderHighlightEvent.Block event) {
        RenderBlockPreview preview = ConstructionWand.instance.renderBlockPreview;
        if(preview == null) return;
        if(preview.renderBlockHighlight(event.getTarget(), event.getCamera(), event.getPoseStack(),
                event.getMultiBufferSource(), event.getPartialTick())) {
            event.setCanceled(true);
        }
    }
}
