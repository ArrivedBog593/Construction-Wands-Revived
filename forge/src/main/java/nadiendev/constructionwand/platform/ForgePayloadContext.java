package nadiendev.constructionwand.platform;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;
import nadiendev.constructionwand.network.IPayloadContext;

import java.util.concurrent.CompletableFuture;

/** Adapts Forge's payload context to the common one. */
public record ForgePayloadContext(CustomPayloadEvent.Context context) implements IPayloadContext {

    @Override
    public Player player() {
        return context.isServerSide() ? context.getSender() : ForgeClientHooks.localPlayer();
    }

    @Override
    public CompletableFuture<Void> enqueueWork(Runnable task) {
        return context.enqueueWork(task);
    }

    @Override
    public void disconnect(Component reason) {
        context.getConnection().disconnect(reason);
    }
}
