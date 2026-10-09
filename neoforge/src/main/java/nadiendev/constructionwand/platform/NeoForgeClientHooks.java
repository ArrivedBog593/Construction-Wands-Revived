//? if neoforge {
package nadiendev.constructionwand.platform;

//? if <1.21.4
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/** Client only calls, kept apart so the dedicated server never has to load the classes they touch. */
final class NeoForgeClientHooks {

    private NeoForgeClientHooks() {
    }

    //? if <1.21.4 {
    static void registerItemProperty(Item item, ResourceLocation name, ItemPropertyFunction function) {
        ItemProperties.register(item, name, function::call);
    }
    //?}

    static void sendToServer(net.minecraft.network.protocol.common.custom.CustomPacketPayload payload) {
        //? if >=1.21.7 {
        /*net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(payload);
        *///?} else {
        net.neoforged.neoforge.network.PacketDistributor.sendToServer(payload);
        //?}
    }
}
//?}
