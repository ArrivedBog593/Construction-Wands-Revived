package nadiendev.constructionwand.platform;

import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.player.Player;
import nadiendev.constructionwand.network.IPayloadContext;

/** Adapts a Fabric networking context to the common one. */
public record FabricPayloadContext(Player player, BlockableEventLoop<?> loop) implements IPayloadContext {

    @Override
    public void enqueueWork(Runnable task) {
        loop.execute(task);
    }
}
