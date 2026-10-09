//? if fabric {
package nadiendev.constructionwand.platform;

import net.minecraft.network.chat.Component;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.player.Player;
import nadiendev.constructionwand.network.IPayloadContext;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/** Adapts a Fabric networking context to the common one. */
public record FabricPayloadContext(Player player, BlockableEventLoop<?> loop, Consumer<Component> disconnector)
        implements IPayloadContext {

    @Override
    public CompletableFuture<Void> enqueueWork(Runnable task) {
        if (!loop.isSameThread()) {
            return loop.submit(task);
        }
        try {
            task.run();
            return CompletableFuture.completedFuture(null);
        } catch (Throwable throwable) {
            return CompletableFuture.failedFuture(throwable);
        }
    }

    @Override
    public void disconnect(Component reason) {
        disconnector.accept(reason);
    }
}
//?}
