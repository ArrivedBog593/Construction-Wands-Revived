package nadiendev.constructionwand;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import nadiendev.constructionwand.basics.ConfigClient;
import nadiendev.constructionwand.basics.ConfigServer;
import nadiendev.constructionwand.basics.ReplacementRegistry;
import nadiendev.constructionwand.network.ModMessages;
import nadiendev.constructionwand.platform.FabricPayloadContext;
import nadiendev.constructionwand.registry.ModRegistries;
import nadiendev.constructionwand.registry.Registrar;

public class ConstructionWandFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ConstructionWand.init();

        for (Registrar<?> registrar : ModRegistries.all()) {
            registerAll(registrar);
        }

        NeoForgeConfigRegistry.INSTANCE.register(ConstructionWand.MODID, ModConfig.Type.SERVER, (ModConfigSpec) ConfigServer.SPEC);
        NeoForgeConfigRegistry.INSTANCE.register(ConstructionWand.MODID, ModConfig.Type.CLIENT, (ModConfigSpec) ConfigClient.SPEC);

        for (ModMessages.Definition<?> definition : ModMessages.PACKETS) {
            registerPacket(definition);
        }

        ConstructionWand.commonSetup();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> ReplacementRegistry.init());
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                ConstructionWand.instance.undoHistory.removePlayer(handler.getPlayer()));
    }

    private static <T> void registerAll(Registrar<T> registrar) {
        Registry<T> registry = registrar.registry();
        registrar.registerAll((id, value) -> Registry.register(registry, id, value));
    }

    private static <T extends CustomPacketPayload> void registerPacket(ModMessages.Definition<T> definition) {
        if (definition.toClient()) {
            // The client side receiver is registered by ConstructionWandFabricClient
            PayloadTypeRegistry.playS2C().register(definition.type(), definition.codec());
        } else {
            PayloadTypeRegistry.playC2S().register(definition.type(), definition.codec());
            ServerPlayNetworking.registerGlobalReceiver(definition.type(), (payload, context) ->
                    definition.handler().accept(payload, new FabricPayloadContext(
                            context.player(), context.server(), reason -> context.responseSender().disconnect(reason))));
        }
    }
}
