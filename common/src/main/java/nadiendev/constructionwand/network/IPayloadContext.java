package nadiendev.constructionwand.network;

import net.minecraft.world.entity.player.Player;

/**
 * Loader independent view of the context a packet is received in. Each loader adapts its own context to it.
 */
public interface IPayloadContext
{
    /** The player that sent the packet (server side) or the local player (client side). */
    Player player();

    /** Runs the task on the main thread of the receiving side. */
    void enqueueWork(Runnable task);
}
