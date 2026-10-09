package nadiendev.constructionwand.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import nadiendev.constructionwand.platform.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Declares every packet of the mod. Each loader walks {@link #PACKETS} and registers them with its own
 * networking system, then uses the send helpers below.
 */
public final class ModMessages {

    /** A packet definition, independent of the loader. */
    public record Definition<T extends CustomPacketPayload>(
            CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
            boolean toClient,
            BiConsumer<T, IPayloadContext> handler) {
    }

    public static final List<Definition<?>> PACKETS = new ArrayList<>();

    static {
        toClient(PacketUndoBlocks.ID, PacketUndoBlocks.CODEC, PacketUndoBlocks.Handler::handle);
        toClient(PacketRefreshModelData.ID, PacketRefreshModelData.CODEC, PacketRefreshModelData.Handler::handle);
        toServer(PacketQueryUndo.ID, PacketQueryUndo.CODEC, PacketQueryUndo.Handler::handle);
        toServer(PacketWandOption.ID, PacketWandOption.CODEC, PacketWandOption.Handler::handle);
        toServer(PacketToggleVoidSack.TYPE, PacketToggleVoidSack.CODEC, PacketToggleVoidSack::handle);
        toServer(PacketToggleVoidSackActive.TYPE, PacketToggleVoidSackActive.CODEC, PacketToggleVoidSackActive::handle);
        toServer(PacketWandUndo.ID, PacketWandUndo.CODEC, PacketWandUndo.Handler::handle);
        toServer(PacketExchangeSelect.ID, PacketExchangeSelect.CODEC, PacketExchangeSelect.Handler::handle);
    }

    private ModMessages() {
    }

    private static <T extends CustomPacketPayload> void toClient(CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, IPayloadContext> handler) {
        PACKETS.add(new Definition<>(type, codec, true, handler));
    }

    private static <T extends CustomPacketPayload> void toServer(CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, IPayloadContext> handler) {
        PACKETS.add(new Definition<>(type, codec, false, handler));
    }

    public static void sendToServer(CustomPacketPayload message) {
        Services.PLATFORM.sendToServer(message);
    }

    public static void sendToPlayer(CustomPacketPayload message, ServerPlayer player) {
        player.connection.send(new ClientboundCustomPayloadPacket(message));
    }

    public static void sendToTrackingChunk(ServerLevel level, ChunkPos chunk, CustomPacketPayload message) {
        Services.PLATFORM.sendToTrackingChunk(level, chunk, message);
    }
}
