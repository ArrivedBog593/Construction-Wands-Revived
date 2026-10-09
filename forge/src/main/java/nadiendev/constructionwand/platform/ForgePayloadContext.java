package nadiendev.constructionwand.platform;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import nadiendev.constructionwand.network.IPayloadContext;

/** Adapts a Forge network context to the common one. */
public record ForgePayloadContext(NetworkEvent.Context context) implements IPayloadContext {

    @Override
    public Player player() {
        if(context.getDirection().getReceptionSide().isServer()) return context.getSender();
        return ForgeClientHooks.localPlayer();
    }

    @Override
    public void enqueueWork(Runnable task) {
        context.enqueueWork(task);
    }
}
