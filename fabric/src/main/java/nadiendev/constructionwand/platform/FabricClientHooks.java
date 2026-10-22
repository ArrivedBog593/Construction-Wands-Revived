package nadiendev.constructionwand.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/** Client only calls, kept apart so the dedicated server never has to load the classes they touch. */
final class FabricClientHooks {

    private FabricClientHooks() {
    }


    static void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}
