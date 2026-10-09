package nadiendev.constructionwand.client.property;

import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.basics.option.WandOptions;
import nadiendev.constructionwand.items.core.ItemCoreAngel;
import nadiendev.constructionwand.items.core.ItemCoreDestruction;
import nadiendev.constructionwand.items.core.ItemCoreExchange;
import nadiendev.constructionwand.items.wand.ItemWand;
import net.minecraft.resources.ResourceLocation;
//? if >=1.21.4 {
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
//?} else {
/*import nadiendev.constructionwand.items.ModItems;
import nadiendev.constructionwand.platform.Services;
import nadiendev.constructionwand.registry.RegistryObject;
import net.minecraft.world.item.Item;
*///?}

/**
 * Tells the wand item models which core is installed. Before 1.21.4 this is an item property predicate used by
 * model overrides; from 1.21.4 on it is a {@code select} property of the data driven item model definitions.
 */
//? if >=1.21.4 {
public class SelectWandCore implements SelectItemModelProperty<String>
{
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ConstructionWand.MODID, "wand_core");

    public static final SelectItemModelProperty.Type<SelectWandCore, String> TYPE =
            SelectItemModelProperty.Type.create(MapCodec.unit(new SelectWandCore()), Codec.STRING);

    @Nullable
    @Override
    public String get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed,
                      ItemDisplayContext displayContext) {
        if (!(stack.getItem() instanceof ItemWand)) return null;
        var core = new WandOptions(stack).cores.get();
        if (core instanceof ItemCoreAngel) return "core_angel";
        if (core instanceof ItemCoreDestruction) return "core_destruction";
        if (core instanceof ItemCoreExchange) return "core_exchange";
        return null;
    }

    //? if >=1.21.5 {
    @Override
    public Codec<String> valueCodec() {
        return Codec.STRING;
    }
    //?}

    @Override
    public Type<? extends SelectItemModelProperty<String>, String> type() {
        return TYPE;
    }
}
//?} else {
/*public class SelectWandCore
{
    public static final ResourceLocation WAND_CORE = ResourceLocation.fromNamespaceAndPath(ConstructionWand.MODID, "wand_core");

    /^* Registers the wand core predicate. ItemProperties#register is not thread-safe, so run it on the main thread. ^/
    public static void register() {
        for (RegistryObject<Item> wand : ModItems.WANDS) {
            Services.PLATFORM.registerItemProperty(wand.get(), WAND_CORE, (stack, level, entity, seed) -> {
                if (!(stack.getItem() instanceof ItemWand)) return 0f;
                var core = new WandOptions(stack).cores.get();
                if (core instanceof ItemCoreAngel)       return 1f; // core_angel active
                if (core instanceof ItemCoreDestruction) return 2f; // core_destruction active
                if (core instanceof ItemCoreExchange)    return 3f; // core_exchange active
                return 0f;                                          // default (no core)
            });
        }
    }
}
*///?}
