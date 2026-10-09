package nadiendev.constructionwand.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.platform.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Declares every packet of the mod. Each loader walks {@link #PACKETS} and registers them with its own
 * networking system, then uses the send helpers below.
 */
public final class ModMessages
{
    /** A packet definition, independent of the loader. */
    public record Definition<T>(
            ResourceLocation id,
            Class<T> type,
            BiConsumer<T, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, T> decoder,
            boolean toClient,
            BiConsumer<T, IPayloadContext> handler) {
    }

    public static final List<Definition<?>> PACKETS = new ArrayList<>();

    static {
        toClient("undo_blocks", PacketUndoBlocks.class, PacketUndoBlocks::encode, PacketUndoBlocks::decode, PacketUndoBlocks.Handler::handle);
        toServer("query_undo", PacketQueryUndo.class, PacketQueryUndo::encode, PacketQueryUndo::decode, PacketQueryUndo.Handler::handle);
        toServer("wand_option", PacketWandOption.class, PacketWandOption::encode, PacketWandOption::decode, PacketWandOption.Handler::handle);
        toServer("exchange_select", PacketExchangeSelect.class, PacketExchangeSelect::encode, PacketExchangeSelect::decode, PacketExchangeSelect.Handler::handle);
        toServer("toggle_void_sack", PacketToggleVoidSack.class, PacketToggleVoidSack::encode, PacketToggleVoidSack::decode, PacketToggleVoidSack.Handler::handle);
        toServer("toggle_void_sack_active", PacketToggleVoidSackActive.class, PacketToggleVoidSackActive::encode, PacketToggleVoidSackActive::decode, PacketToggleVoidSackActive.Handler::handle);
        toServer("wand_undo", PacketWandUndo.class, PacketWandUndo::encode, PacketWandUndo::decode, PacketWandUndo.Handler::handle);
    }

    private ModMessages() {
    }

    private static <T> void toClient(String name, Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder,
                                     Function<FriendlyByteBuf, T> decoder, BiConsumer<T, IPayloadContext> handler) {
        PACKETS.add(new Definition<>(ConstructionWand.loc(name), type, encoder, decoder, true, handler));
    }

    private static <T> void toServer(String name, Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder,
                                     Function<FriendlyByteBuf, T> decoder, BiConsumer<T, IPayloadContext> handler) {
        PACKETS.add(new Definition<>(ConstructionWand.loc(name), type, encoder, decoder, false, handler));
    }

    /** Finds the definition of the class of a packet instance. */
    @SuppressWarnings("unchecked")
    public static <T> Definition<T> definitionOf(T packet) {
        for(Definition<?> definition : PACKETS) {
            if(definition.type() == packet.getClass()) return (Definition<T>) definition;
        }
        throw new IllegalArgumentException("Unregistered packet " + packet.getClass().getName());
    }

    public static void sendToServer(Object message) {
        Services.PLATFORM.sendToServer(message);
    }

    public static void sendToPlayer(Object message, ServerPlayer player) {
        Services.PLATFORM.sendToPlayer(player, message);
    }
}
