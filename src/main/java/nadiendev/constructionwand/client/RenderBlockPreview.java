package nadiendev.constructionwand.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
//? if >=1.21.2 {
/*import net.minecraft.client.renderer.ShapeRenderer;
*///?} else {
import net.minecraft.client.renderer.LevelRenderer;
//?}
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.client.Camera;
import nadiendev.constructionwand.basics.WandUtil;
import nadiendev.constructionwand.items.wand.ItemWand;
import nadiendev.constructionwand.wand.WandJob;

import java.util.Set;

public class RenderBlockPreview
{
    private WandJob wandJob;
    public Set<BlockPos> undoBlocks;

    /**
     * Renders the placement preview instead of the vanilla block outline.
     *
     * @return true when the vanilla outline must be skipped
     */
    public boolean renderBlockHighlight(BlockHitResult rtr, Camera camera, PoseStack ms, MultiBufferSource buffer,
                                        double partialTicks) {
        if(rtr.getType() != HitResult.Type.BLOCK) return false;

        Entity entity = camera.getEntity();
        if(!(entity instanceof Player player)) return false;
        Set<BlockPos> blocks;
        float colorR = 0, colorG = 0, colorB = 0;

        ItemStack wand = WandUtil.holdingWand(player);
        if(wand == null) return false;

        if(!(player.isCrouching() && ClientEvents.isOptKeyDown())) {
            // Use cached wandJob for previews of the same target pos/dir
            // Exception: always update if blockCount < 2 to prevent 1-block previews when block updates
            // from the last placement are lagging
            if(wandJob == null || !compareRTR(wandJob.rayTraceResult, rtr) || !(wandJob.wand.equals(wand))
                || wandJob.blockCount() < 2) {
                wandJob = ItemWand.getWandJob(player, player.level(), rtr, wand);
            }
            blocks = wandJob.getBlockPositions();
        }
        else {
            blocks = undoBlocks;
            colorG = 1;
        }

        if(blocks == null || blocks.isEmpty()) return false;

        VertexConsumer lineBuilder = buffer.getBuffer(RenderType.LINES);

        double d0 = player.xOld + (player.getX() - player.xOld) * partialTicks;
        double d1 = player.yOld + player.getEyeHeight() + (player.getY() - player.yOld) * partialTicks;
        double d2 = player.zOld + (player.getZ() - player.zOld) * partialTicks;

        for(BlockPos block : blocks) {
            AABB aabb = new AABB(block).move(-d0, -d1, -d2);
            //? if >=1.21.2 {
            /*ShapeRenderer.renderLineBox(ms, lineBuilder, aabb, colorR, colorG, colorB, 0.4F);
            *///?} else {
            LevelRenderer.renderLineBox(ms, lineBuilder, aabb, colorR, colorG, colorB, 0.4F);
            //?}
        }

        return true;
    }

    public void reset() {
        wandJob = null;
    }

    private static boolean compareRTR(BlockHitResult rtr1, BlockHitResult rtr2) {
        return rtr1.getBlockPos().equals(rtr2.getBlockPos()) && rtr1.getDirection().equals(rtr2.getDirection());
    }
}
