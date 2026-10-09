package nadiendev.constructionwand.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import nadiendev.constructionwand.ConstructionWand;

import java.util.HashSet;
import java.util.Set;

public class PacketUndoBlocks
{
    public HashSet<BlockPos> undoBlocks;

    public PacketUndoBlocks(Set<BlockPos> undoBlocks) {
        this.undoBlocks = new HashSet<>(undoBlocks);
    }

    private PacketUndoBlocks(HashSet<BlockPos> undoBlocks) {
        this.undoBlocks = undoBlocks;
    }

    public static void encode(PacketUndoBlocks msg, FriendlyByteBuf buffer) {
        for(BlockPos pos : msg.undoBlocks) {
            buffer.writeBlockPos(pos);
        }
    }

    public static PacketUndoBlocks decode(FriendlyByteBuf buffer) {
        HashSet<BlockPos> undoBlocks = new HashSet<>();

        while(buffer.isReadable()) {
            undoBlocks.add(buffer.readBlockPos());
        }
        return new PacketUndoBlocks(undoBlocks);
    }

    public static class Handler
    {
        public static void handle(final PacketUndoBlocks msg, final IPayloadContext ctx) {
            ctx.enqueueWork(() -> {
                if(ConstructionWand.instance.renderBlockPreview != null)
                    ConstructionWand.instance.renderBlockPreview.undoBlocks = msg.undoBlocks;
            });
        }
    }
}
