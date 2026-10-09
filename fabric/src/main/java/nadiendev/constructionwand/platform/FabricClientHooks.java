package nadiendev.constructionwand.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/** Client only calls, kept apart so the dedicated server never has to load the classes they touch. */
final class FabricClientHooks {

    private FabricClientHooks() {
    }

    static void registerItemProperty(Item item, ResourceLocation name, ItemPropertyFunction function) {
        ItemProperties.register(item, name, function::call);
    }

    static void sendToServer(ResourceLocation id, FriendlyByteBuf buffer) {
        ClientPlayNetworking.send(id, buffer);
    }
}
