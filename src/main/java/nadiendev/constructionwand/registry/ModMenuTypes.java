package nadiendev.constructionwand.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.MenuType;
import nadiendev.constructionwand.items.containeritems.MenuVoidSack;
import nadiendev.constructionwand.platform.Services;

public class ModMenuTypes
{
    public static final Registrar<MenuType<?>> MENU_TYPES = new Registrar<>(BuiltInRegistries.MENU);

    public static final RegistryObject<MenuType<MenuVoidSack>> VOID_SACK =
            MENU_TYPES.register("void_sack", () ->
                    Services.PLATFORM.<MenuVoidSack>createMenuType((windowId, inv, data) -> {
                        InteractionHand hand = data.readBoolean()
                                ? InteractionHand.MAIN_HAND
                                : InteractionHand.OFF_HAND;
                        return new MenuVoidSack(windowId, inv, hand);
                    }));

}
