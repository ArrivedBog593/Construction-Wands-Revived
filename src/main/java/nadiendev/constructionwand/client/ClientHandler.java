//? if neoforge {
package nadiendev.constructionwand.client;

import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.client.property.SelectWandCore;
import nadiendev.constructionwand.client.screen.ScreenVoidSack;
import nadiendev.constructionwand.registry.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
//? if >=1.21.4
//import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;

@EventBusSubscriber(modid = ConstructionWand.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ConstructionWand.instance.renderBlockPreview = new RenderBlockPreview();
        //? if <1.21.4 {
        // ItemProperties#register is not thread-safe, must run inside enqueueWork
        event.enqueueWork(SelectWandCore::register);
        //?}
    }

    @SubscribeEvent
    public static void registerKeymapping(final RegisterKeyMappingsEvent event) {
        event.register(KeybindHandler.KEY_VOID_SACK_TOGGLE);
        event.register(KeybindHandler.KEY_WAND_UNDO);
        event.register(KeybindHandler.KEY_EXCHANGE_SELECT);
    }

    //? if >=1.21.4 {
    /*@SubscribeEvent
    public static void registerModelProperties(RegisterSelectItemModelPropertyEvent event) {
        event.register(SelectWandCore.ID, SelectWandCore.TYPE);
    }
    *///?}

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.VOID_SACK.get(), ScreenVoidSack::new);
    }
}
//?}
