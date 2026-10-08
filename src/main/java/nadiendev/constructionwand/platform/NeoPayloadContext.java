//? if neoforge {
/*package nadiendev.constructionwand.platform;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import nadiendev.constructionwand.network.IPayloadContext;

import java.util.concurrent.CompletableFuture;

/^* Adapts NeoForge's payload context to the common one. ^/
public record NeoPayloadContext(net.neoforged.neoforge.network.handling.IPayloadContext context) implements IPayloadContext {

    @Override
    public Player player() {
        return context.player();
    }

    @Override
    public CompletableFuture<Void> enqueueWork(Runnable task) {
        return context.enqueueWork(task);
    }

    @Override
    public void disconnect(Component reason) {
        context.disconnect(reason);
    }
}
*///?}
