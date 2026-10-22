package nadiendev.constructionwand.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.network.ClientRefresh;

/** Forwards NeoForge's client side game events to the shared client hooks. */
@EventBusSubscriber(modid = ConstructionWand.MODID, value = Dist.CLIENT)
public final class NeoForgeClientEvents {

    private NeoForgeClientEvents() {}

    @SubscribeEvent
    public static void onKey(InputEvent.Key event) {
        ClientEvents.onKey();
        KeybindHandler.onKey();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (ClientEvents.onMouseScroll(event.getScrollDeltaY())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        ClientEvents.onLeftClickEmpty(event.getEntity(), event.getItemStack());
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getSide().isServer()) return;
        if (ClientEvents.onRightClickItem(event.getEntity(), event.getItemStack())) event.setCanceled(true);
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void onRenderHighlight(RenderHighlightEvent.Block event) {
        RenderBlockPreview preview = ConstructionWand.instance.renderBlockPreview;
        if (preview == null) return;
        double partialTicks = event.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        if (preview.renderBlockHighlight(event.getTarget(), event.getCamera(), event.getPoseStack(),
                event.getMultiBufferSource(), partialTicks)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        ClientRefresh.onClientTick();
    }
}
