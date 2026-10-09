package nadiendev.constructionwand.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.client.property.SelectWandCore;
import nadiendev.constructionwand.client.screen.ScreenVoidSack;
import nadiendev.constructionwand.items.ModItems;
import nadiendev.constructionwand.registry.ModMenuTypes;
import nadiendev.constructionwand.registry.RegistryObject;

/** Client side setup on the mod event bus. */
@Mod.EventBusSubscriber(modid = ConstructionWand.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientHandler
{
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ConstructionWand.instance.renderBlockPreview = new RenderBlockPreview();

        // ItemProperties#register and MenuScreens#register are not thread-safe, so they have to run in enqueueWork
        event.enqueueWork(SelectWandCore::register);
        event.enqueueWork(() -> MenuScreens.register(ModMenuTypes.VOID_SACK.get(), ScreenVoidSack::new));
    }

    @SubscribeEvent
    public static void registerKeyMappings(final RegisterKeyMappingsEvent event) {
        KeybindHandler.KEY_VOID_SACK_TOGGLE.setKeyConflictContext(KeyConflictContext.IN_GAME);
        KeybindHandler.KEY_EXCHANGE_SELECT.setKeyConflictContext(KeyConflictContext.IN_GAME);
        KeybindHandler.KEY_WAND_UNDO.setKeyConflictContext(KeyConflictContext.IN_GAME);

        event.register(KeybindHandler.KEY_VOID_SACK_TOGGLE);
        event.register(KeybindHandler.KEY_EXCHANGE_SELECT);
        event.register(KeybindHandler.KEY_WAND_UNDO);
    }

    @SubscribeEvent
    public static void registerItemColors(final RegisterColorHandlersEvent.Item event) {
        WandItemColor color = new WandItemColor();
        for(RegistryObject<Item> itemSupplier : ModItems.WANDS) {
            event.register(color, itemSupplier.get());
        }
    }
}
