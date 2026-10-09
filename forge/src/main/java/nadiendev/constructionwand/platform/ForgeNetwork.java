package nadiendev.constructionwand.platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.network.ModMessages;

import java.util.Optional;

/** Registers the packets declared in {@link ModMessages} on a Forge simple channel. */
public final class ForgeNetwork {
    private static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel CHANNEL;

    private ForgeNetwork() {
    }

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(ConstructionWand.MODID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals);

        int index = 0;
        for(ModMessages.Definition<?> definition : ModMessages.PACKETS) {
            registerPacket(index++, definition);
        }
    }

    private static <T> void registerPacket(int index, ModMessages.Definition<T> definition) {
        CHANNEL.registerMessage(index, definition.type(), definition.encoder()::accept, definition.decoder()::apply,
                (message, contextSupplier) -> {
                    definition.handler().accept(message, new ForgePayloadContext(contextSupplier.get()));
                    contextSupplier.get().setPacketHandled(true);
                },
                Optional.of(definition.toClient() ? NetworkDirection.PLAY_TO_CLIENT : NetworkDirection.PLAY_TO_SERVER));
    }

    static void sendToServer(Object message) {
        CHANNEL.sendToServer(message);
    }

    static void sendToPlayer(ServerPlayer player, Object message) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
