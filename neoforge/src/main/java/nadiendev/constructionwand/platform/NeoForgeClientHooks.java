package nadiendev.constructionwand.platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/** Client only calls, kept apart so the dedicated server never has to load the classes they touch. */
final class NeoForgeClientHooks {

    private NeoForgeClientHooks() {
    }


    static void sendToServer(net.minecraft.network.protocol.common.custom.CustomPacketPayload payload) {
        net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(payload);
    }
}
