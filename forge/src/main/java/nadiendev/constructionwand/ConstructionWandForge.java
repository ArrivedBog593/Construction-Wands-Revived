package nadiendev.constructionwand;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import nadiendev.constructionwand.basics.ConfigClient;
import nadiendev.constructionwand.basics.ConfigServer;
import nadiendev.constructionwand.containers.ContainerRegistrar;
import nadiendev.constructionwand.platform.ForgeNetwork;
import nadiendev.constructionwand.registry.ModRegistries;
import nadiendev.constructionwand.registry.Registrar;

@Mod(ConstructionWand.MODID)
public class ConstructionWandForge
{
    public ConstructionWandForge() {
        ConstructionWand.init();

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::register);

        // Config setup
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, (ForgeConfigSpec) ConfigServer.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, (ForgeConfigSpec) ConfigClient.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ConstructionWand.commonSetup();

        // Register packets
        ForgeNetwork.register();

        // Container handlers of optional mods
        ContainerRegistrar.register();
    }

    private void register(final RegisterEvent event) {
        for(Registrar<?> registrar : ModRegistries.all()) {
            registerAll(event, registrar);
        }
    }

    private static <T> void registerAll(RegisterEvent event, Registrar<T> registrar) {
        event.register(registrar.registry().key(), helper -> registrar.registerAll(helper::register));
    }
}
