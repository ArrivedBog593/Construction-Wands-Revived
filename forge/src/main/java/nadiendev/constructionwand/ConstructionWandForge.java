package nadiendev.constructionwand;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import nadiendev.constructionwand.basics.ConfigClient;
import nadiendev.constructionwand.basics.ConfigServer;
import nadiendev.constructionwand.basics.ReplacementRegistry;
import nadiendev.constructionwand.client.ForgeClientEvents;
import nadiendev.constructionwand.events.VoidSackDropHandler;
import nadiendev.constructionwand.events.VoidSackPickupHandler;
import nadiendev.constructionwand.platform.ForgeNetwork;
import nadiendev.constructionwand.registry.ModRegistries;
import nadiendev.constructionwand.registry.Registrar;

@Mod(ConstructionWand.MODID)
public class ConstructionWandForge {

    public ConstructionWandForge(FMLJavaModLoadingContext context) {
        ConstructionWand.init();

        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::register);

        context.registerConfig(ModConfig.Type.SERVER, (ForgeConfigSpec) ConfigServer.SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, (ForgeConfigSpec) ConfigClient.SPEC);

        ForgeNetwork.init();

        IEventBus gameBus = MinecraftForge.EVENT_BUS;
        gameBus.addListener((ServerStartingEvent event) -> ReplacementRegistry.init());
        gameBus.addListener((PlayerEvent.PlayerLoggedOutEvent event) ->
                ConstructionWand.instance.undoHistory.removePlayer(event.getEntity()));
        gameBus.addListener(EventPriority.HIGH, (EntityJoinLevelEvent event) -> {
            if (VoidSackDropHandler.onEntityJoinLevel(event.getLevel(), event.getEntity())) {
                event.setCanceled(true);
            }
        });
        gameBus.addListener((EntityItemPickupEvent event) -> {
            if (VoidSackPickupHandler.onItemPickup(event.getEntity(), event.getItem())) {
                event.setCanceled(true);
            }
        });

        if (FMLEnvironment.dist.isClient()) {
            ForgeClientEvents.init(modBus, gameBus);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ConstructionWand.commonSetup();
    }

    private void register(final RegisterEvent event) {
        for (Registrar<?> registrar : ModRegistries.all()) {
            registerAll(event, registrar);
        }
    }

    private static <T> void registerAll(RegisterEvent event, Registrar<T> registrar) {
        event.register(registrar.registry().key(), helper -> registrar.registerAll(helper::register));
    }
}
