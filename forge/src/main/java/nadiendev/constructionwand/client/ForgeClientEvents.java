package nadiendev.constructionwand.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.client.property.SelectWandCore;
import nadiendev.constructionwand.client.screen.ScreenVoidSack;
import nadiendev.constructionwand.network.ClientRefresh;
import nadiendev.constructionwand.registry.ModMenuTypes;

/** Client side wiring for Forge. Only loaded on the physical client. */
public final class ForgeClientEvents {

    private ForgeClientEvents() {
    }

    public static void init(IEventBus modBus, IEventBus gameBus) {
        modBus.addListener(ForgeClientEvents::onClientSetup);
        modBus.addListener(ForgeClientEvents::registerKeyMappings);

        gameBus.addListener(ForgeClientEvents::onKey);
        gameBus.addListener(EventPriority.HIGHEST, ForgeClientEvents::onMouseScroll);
        gameBus.addListener(ForgeClientEvents::onLeftClickEmpty);
        gameBus.addListener(ForgeClientEvents::onRightClickItem);
        gameBus.addListener(EventPriority.NORMAL, true, ForgeClientEvents::onRenderHighlight);
        gameBus.addListener(ForgeClientEvents::onClientTick);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        ConstructionWand.instance.renderBlockPreview = new RenderBlockPreview();
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.VOID_SACK.get(), ScreenVoidSack::new);
            SelectWandCore.register();
        });
    }

    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeybindHandler.KEY_VOID_SACK_TOGGLE);
        event.register(KeybindHandler.KEY_WAND_UNDO);
        event.register(KeybindHandler.KEY_EXCHANGE_SELECT);
    }

    private static void onKey(InputEvent.Key event) {
        ClientEvents.onKey();
        KeybindHandler.onKey();
    }

    private static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (ClientEvents.onMouseScroll(event.getDeltaY())) event.setCanceled(true);
    }

    private static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        ClientEvents.onLeftClickEmpty(event.getEntity(), event.getItemStack());
    }

    private static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getSide().isServer()) return;
        if (ClientEvents.onRightClickItem(event.getEntity(), event.getItemStack())) event.setCanceled(true);
    }

    private static void onRenderHighlight(RenderHighlightEvent.Block event) {
        RenderBlockPreview preview = ConstructionWand.instance.renderBlockPreview;
        if (preview == null) return;
        if (preview.renderBlockHighlight(event.getTarget(), event.getCamera(), event.getPoseStack(),
                event.getMultiBufferSource(), event.getPartialTick())) {
            event.setCanceled(true);
        }
    }

    private static void onClientTick(TickEvent.ClientTickEvent.Post event) {
        ClientRefresh.onClientTick();
    }
}
