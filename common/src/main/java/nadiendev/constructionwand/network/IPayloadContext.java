package nadiendev.constructionwand.network;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.CompletableFuture;

/**
 * Loader independent view of the context a packet is received in. Each loader adapts its own context to it.
 */
public interface IPayloadContext {

    /** The player that sent the packet (server side) or the local player (client side). */
    Player player();

    /** Runs the task on the main thread of the receiving side. Failures complete the future exceptionally. */
    CompletableFuture<Void> enqueueWork(Runnable task);

    /** Disconnects the sender with the given reason. */
    void disconnect(Component reason);
}
