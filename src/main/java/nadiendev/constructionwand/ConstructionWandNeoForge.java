//? if neoforge {
/*package nadiendev.constructionwand;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;
import nadiendev.constructionwand.basics.ConfigClient;
import nadiendev.constructionwand.basics.ConfigServer;
import nadiendev.constructionwand.containers.ContainerRegistrar;
import nadiendev.constructionwand.network.ModMessages;
import nadiendev.constructionwand.platform.NeoPayloadContext;
import nadiendev.constructionwand.registry.ModRegistries;
import nadiendev.constructionwand.registry.Registrar;

@Mod(ConstructionWand.MODID)
public class ConstructionWandNeoForge {

    public ConstructionWandNeoForge(IEventBus modBus, ModContainer container) {
        ConstructionWand.init();

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::registerPayloads);
        modBus.addListener(this::register);

        container.registerConfig(ModConfig.Type.SERVER, (ModConfigSpec) ConfigServer.SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, (ModConfigSpec) ConfigClient.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ConstructionWand.commonSetup();
        ContainerRegistrar.register();
    }

    private void register(final RegisterEvent event) {
        for (Registrar<?> registrar : ModRegistries.all()) {
            registerAll(event, registrar);
        }
    }

    private static <T> void registerAll(RegisterEvent event, Registrar<T> registrar) {
        event.register(registrar.registry().key(), helper -> registrar.registerAll(helper::register));
    }

    private void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(ConstructionWand.MODID);
        for (ModMessages.Definition<?> definition : ModMessages.PACKETS) {
            registerPacket(registrar, definition);
        }
    }

    private static <T extends CustomPacketPayload> void registerPacket(PayloadRegistrar registrar,
                                                                       ModMessages.Definition<T> definition) {
        IPayloadHandler<T> handler = (payload, context) -> definition.handler().accept(payload, new NeoPayloadContext(context));
        if (definition.toClient()) {
            registrar.playToClient(definition.type(), definition.codec(), handler);
        } else {
            registrar.playToServer(definition.type(), definition.codec(), handler);
        }
    }
}
*///?}
