package nadiendev.constructionwand.creative;

import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.items.ModItems;
import nadiendev.constructionwand.platform.Services;
import nadiendev.constructionwand.registry.Registrar;
import nadiendev.constructionwand.registry.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {

    public static final Registrar<CreativeModeTab> CREATIVE_TABS = new Registrar<>(BuiltInRegistries.CREATIVE_MODE_TAB);

    public static final RegistryObject<CreativeModeTab> CONSTRUCTION_WAND_TAB =
            CREATIVE_TABS.register("construction_wand_tab", () -> Services.PLATFORM.newCreativeTabBuilder()
                    .title(Component.translatable("itemGroup." + ConstructionWand.MODID))
                    .icon(() -> new ItemStack(ModItems.WAND_INFINITY.get()))
                    .displayItems((parameters, output) -> {
                        for (RegistryObject<? extends Item> entry : ModItems.ITEMS.entries()) {
                            output.accept(entry.get());
                        }
                    })
                    .build());

}
