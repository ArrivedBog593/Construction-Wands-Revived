package nadiendev.constructionwand.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nadiendev.constructionwand.basics.ConfigClient;
import nadiendev.constructionwand.basics.WandUtil;
import nadiendev.constructionwand.basics.option.WandOptions;
import nadiendev.constructionwand.items.wand.ItemWand;
import nadiendev.constructionwand.network.ModMessages;
import nadiendev.constructionwand.network.PacketQueryUndo;
import nadiendev.constructionwand.network.PacketWandOption;

public class ClientEvents
{
    private static boolean optPressed;

    /** Called by the loaders whenever keyboard input is processed (or every client tick). */
    public static void onKey() {
        Player player = Minecraft.getInstance().player;
        if(player == null) return;
        if(WandUtil.holdingWand(player) == null) return;

        boolean optState = isOptKeyDown();
        if(optPressed != optState) {
            optPressed = optState;
            ModMessages.sendToServer(new PacketQueryUndo(optPressed));
            //ConstructionWand.LOGGER.debug("OPT key update: " + optPressed);
        }
    }

    /**
     * Sneak+(OPT)+Scroll to change direction lock.
     *
     * @return true when the scroll was consumed and must not reach the hotbar
     */
    public static boolean onMouseScroll(double scroll) {
        Player player = Minecraft.getInstance().player;

        if(player == null || !modeKeyCombDown(player) || scroll == 0) return false;

        ItemStack wand = WandUtil.holdingWand(player);
        if(wand == null) return false;

        WandOptions wandOptions = new WandOptions(wand);
        wandOptions.lock.next(scroll < 0);
        ModMessages.sendToServer(new PacketWandOption(wandOptions.lock, true));
        return true;
    }

    /** Sneak+(OPT)+Left click wand to change core. */
    public static void onLeftClickEmpty(Player player, ItemStack wand) {
        if(player == null || !modeKeyCombDown(player)) return;

        if(!(wand.getItem() instanceof ItemWand)) return;

        WandOptions wandOptions = new WandOptions(wand);
        wandOptions.cores.next();
        ModMessages.sendToServer(new PacketWandOption(wandOptions.cores, true));
    }

    /**
     * Sneak+(OPT)+Right click wand to open GUI. Only call on the logical client.
     *
     * @return true when the GUI was opened and the interaction must be cancelled
     */
    public static boolean onRightClickItem(Player player, ItemStack wand) {
        if(player == null || !guiKeyCombDown(player)) return false;
        if(!(wand.getItem() instanceof ItemWand)) return false;

        Minecraft.getInstance().gui.setScreen(new ScreenWand(wand));
        return true;
    }

    private static boolean isKeyDown(int id) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), id);
    }

    public static boolean isOptKeyDown() {
        return isKeyDown(ConfigClient.OPT_KEY.get());
    }

    public static boolean modeKeyCombDown(Player player) {
        return player.isCrouching() && (isOptKeyDown() || !ConfigClient.SHIFTOPT_MODE.get());
    }

    public static boolean guiKeyCombDown(Player player) {
        return player.isCrouching() && (isOptKeyDown() || !ConfigClient.SHIFTOPT_GUI.get());
    }
}
