//? if neoforge {
package nadiendev.constructionwand.platform;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/** Client only calls, kept apart so the dedicated server never has to load the classes they touch. */
final class NeoForgeClientHooks {

    private NeoForgeClientHooks() {
    }

    static void registerItemProperty(Item item, ResourceLocation name, ItemPropertyFunction function) {
        ItemProperties.register(item, name, function::call);
    }
}
//?}
