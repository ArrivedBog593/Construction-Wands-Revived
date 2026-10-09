//? if fabric {
/*package nadiendev.constructionwand;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import nadiendev.constructionwand.basics.ConfigClient;
import nadiendev.constructionwand.basics.ConfigServer;
import nadiendev.constructionwand.basics.ReplacementRegistry;
import nadiendev.constructionwand.network.ModMessages;
import nadiendev.constructionwand.platform.FabricConfigSpec;
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

        // Fabric has no config system, so both configs are plain files in the config folder
        ((FabricConfigSpec) ConfigServer.SPEC).load(FabricLoader.getInstance().getConfigDir().resolve(ConstructionWand.MODID + "-server.toml"));
        ((FabricConfigSpec) ConfigClient.SPEC).load(FabricLoader.getInstance().getConfigDir().resolve(ConstructionWand.MODID + "-client.toml"));

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
            PayloadTypeRegistry.clientboundPlay().register(definition.type(), definition.codec());
        } else {
            PayloadTypeRegistry.serverboundPlay().register(definition.type(), definition.codec());
            ServerPlayNetworking.registerGlobalReceiver(definition.type(), (payload, context) ->
                    definition.handler().accept(payload, new FabricPayloadContext(
                            context.player(), context.server(), reason -> context.responseSender().disconnect(reason))));
        }
    }
}
*///?}
