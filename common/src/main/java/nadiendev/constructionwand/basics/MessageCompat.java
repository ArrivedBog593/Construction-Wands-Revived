package nadiendev.constructionwand.basics;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

/** Shows a message above the hotbar; 26.1 split {@code displayClientMessage} into dedicated methods. */
public final class MessageCompat {

    private MessageCompat() {
    }

    public static void overlay(Player player, Component message, boolean overlay) {
        //? if >=26.1 {
        /*if (overlay) player.sendOverlayMessage(message);
        else player.sendSystemMessage(message);
        *///?} else {
        player.displayClientMessage(message, overlay);
        //?}
    }
}
