package nadiendev.constructionwand.client;

//? if <26.3
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.items.containeritems.ItemVoidSack;
import nadiendev.constructionwand.network.PacketExchangeSelect;
import nadiendev.constructionwand.network.PacketToggleVoidSackActive;

public class KeybindHandler {

    //? if >=26.3 {
    /*// 26.3 reports scancodes instead of GLFW key codes
    private static final int KEY_M = com.mojang.blaze3d.platform.InputConstants.KEY_M;
    private static final int KEY_K = com.mojang.blaze3d.platform.InputConstants.KEY_K;
    private static final int KEY_KP_7 = com.mojang.blaze3d.platform.InputConstants.KEY_NUMPAD7;
    *///?} else {
    private static final int KEY_M = GLFW.GLFW_KEY_M;
    private static final int KEY_K = GLFW.GLFW_KEY_K;
    private static final int KEY_KP_7 = GLFW.GLFW_KEY_KP_7;
    //?}

    //? if >=1.21.9 {
    /*public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(ConstructionWand.loc("main"));

    *///?}
    public static final KeyMapping KEY_VOID_SACK_TOGGLE = new KeyMapping(
            getKey("void_sack_toggle"),
            KEY_M,
            getCategory("constructionwand")
    );

    public static final KeyMapping KEY_WAND_UNDO = new KeyMapping(
            getKey("wand_undo"),
            KEY_K,
            getCategory("constructionwand")
    );

    /**
     * Selecciona el bloque apuntado como reemplazo del Exchange core.
     * Numpad 7 por defecto (evita el conflicto de Shift+Click con el GUI de la vara).
     */
    public static final KeyMapping KEY_EXCHANGE_SELECT = new KeyMapping(
            getKey("exchange_select"),
            KEY_KP_7,
            getCategory("constructionwand")
    );

    private static String getKey(String name) {
        return String.join(".", "key", ConstructionWand.MODID, name);
    }

    //? if >=1.21.9 {
    /*private static KeyMapping.Category getCategory(String modid) {
        return CATEGORY;
    }
    *///?} else {
    private static String getCategory(String modid) {
        return "key.categories." + modid;
    }
    //?}

    /** Called by the loaders whenever keyboard input is processed (or every client tick). */
    public static void onKey() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        // Toggle Void Sack con tecla M
        if (KEY_VOID_SACK_TOGGLE.consumeClick()) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.getItem() instanceof ItemVoidSack) {
                    PacketToggleVoidSackActive.send(hand);
                    break;
                }
            }
        }

        // Undo de varita con tecla K
        if (KEY_WAND_UNDO.consumeClick()) {
            if (nadiendev.constructionwand.basics.WandUtil.holdingWand(player) != null) {
                nadiendev.constructionwand.network.ModMessages.sendToServer(
                        new nadiendev.constructionwand.network.PacketWandUndo());
            }
        }

        // Seleccionar bloque de reemplazo del Exchange core con Numpad 7
        if (KEY_EXCHANGE_SELECT.consumeClick()) {
            if (nadiendev.constructionwand.basics.WandUtil.holdingWand(player) != null) {
                nadiendev.constructionwand.network.ModMessages.sendToServer(
                        new PacketExchangeSelect());
            }
        }
    }
}