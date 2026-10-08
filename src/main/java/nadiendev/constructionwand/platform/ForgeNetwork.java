//? if forge {
package nadiendev.constructionwand.platform;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.payload.PayloadFlow;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.network.ModMessages;

/** Wraps Forge's payload channel, which carries vanilla custom payloads, for the shared packets. */
public final class ForgeNetwork {

    private static Channel<CustomPacketPayload> channel;

    private ForgeNetwork() {
    }

    /** Builds the channel and registers every packet. Must run during mod construction. */
    public static void init() {
        PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> flow = ChannelBuilder
                .named(ConstructionWand.loc("main"))
                .networkProtocolVersion(1)
                .optional()
                .payloadChannel()
                .play()
                .flow(PacketFlow.CLIENTBOUND);

        for (ModMessages.Definition<?> definition : ModMessages.PACKETS) {
            flow = add(flow, definition);
        }
        channel = flow.build();
    }

    @SuppressWarnings("unchecked")
    private static <T extends CustomPacketPayload> PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> add(
            PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> flow, ModMessages.Definition<T> definition) {
        return flow
                .flow(definition.toClient() ? PacketFlow.CLIENTBOUND : PacketFlow.SERVERBOUND)
                .add(definition.type(), (StreamCodec<RegistryFriendlyByteBuf, T>) definition.codec(), (payload, context) -> {
                    definition.handler().accept(payload, new ForgePayloadContext(context));
                    context.setPacketHandled(true);
                });
    }

    public static void sendToServer(CustomPacketPayload payload) {
        channel.send(payload, PacketDistributor.SERVER.noArg());
    }

    public static void sendToTrackingChunk(ServerLevel level, ChunkPos chunk, CustomPacketPayload payload) {
        channel.send(payload, PacketDistributor.TRACKING_CHUNK.with(level.getChunk(chunk.x, chunk.z)));
    }
}
//?}
