package nadiendev.constructionwand.network;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import nadiendev.constructionwand.basics.WandUtil;
import nadiendev.constructionwand.platform.Services;
import nadiendev.constructionwand.wand.action.ActionExchange;

import static nadiendev.constructionwand.ConstructionWand.MODID;

public class PacketExchangeSelect
{
    public PacketExchangeSelect() {}

    public static void encode(PacketExchangeSelect msg, FriendlyByteBuf buffer) {}

    public static PacketExchangeSelect decode(FriendlyByteBuf buffer) {
        return new PacketExchangeSelect();
    }

    public static class Handler
    {
        public static void handle(final PacketExchangeSelect msg, final IPayloadContext ctx) {
            ctx.enqueueWork(() -> {
                if(!(ctx.player() instanceof ServerPlayer player)) return;

                ItemStack wand = WandUtil.holdingWand(player);
                if(wand == null) return;

                HitResult hit = player.pick(Services.PLATFORM.getBlockReach(player), 1.0F, false);

                if(!(hit instanceof BlockHitResult blockHit) || hit.getType() != HitResult.Type.BLOCK) {
                    player.displayClientMessage(
                            Component.translatable(MODID + ".message.exchange_no_target")
                                    .withStyle(ChatFormatting.RED), true);
                    return;
                }

                BlockPos pos = blockHit.getBlockPos();
                ActionExchange.selectReplacementBlock(player.level(), player, pos);
            });
        }
    }
}
