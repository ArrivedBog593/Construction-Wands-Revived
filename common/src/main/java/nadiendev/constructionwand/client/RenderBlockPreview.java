package nadiendev.constructionwand.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
//? if >=26.2 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
*///?} elif >=1.21.2 {
import net.minecraft.client.renderer.ShapeRenderer;
//?} else {
/*import net.minecraft.client.renderer.LevelRenderer;
*///?}
//? if <26.2
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
    /** Whether the last call replaced the vanilla outline; the outline is drawn in two passes since 1.21.9. */
    public boolean blockOutlineHandled;
    public Set<BlockPos> undoBlocks;

    /**
     * Renders the placement preview instead of the vanilla block outline.
     *
     * @return true when the vanilla outline must be skipped
     */
    //? if >=26.2 {
    /*public boolean renderBlockHighlight(BlockHitResult rtr, Camera camera, PoseStack ms, SubmitNodeCollector buffer,
                                        double partialTicks) {
    *///?} else {
    public boolean renderBlockHighlight(BlockHitResult rtr, Camera camera, PoseStack ms, MultiBufferSource buffer,
                                        double partialTicks) {
    //?}
        return blockOutlineHandled = renderPreview(rtr, camera, ms, buffer, partialTicks);
    }

    //? if >=26.2 {
    /*private boolean renderPreview(BlockHitResult rtr, Camera camera, PoseStack ms, SubmitNodeCollector buffer,
                                  double partialTicks) {
    *///?} else {
    private boolean renderPreview(BlockHitResult rtr, Camera camera, PoseStack ms, MultiBufferSource buffer,
                                  double partialTicks) {
    //?}
        if(rtr.getType() != HitResult.Type.BLOCK) return false;

        //? if >=1.21.11 {
        /*Entity entity = camera.entity();
        *///?} else {
        Entity entity = camera.getEntity();
        //?}
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

        //? if >=26.2 {
        /*double d0 = camera.position().x();
        double d1 = camera.position().y();
        double d2 = camera.position().z();
        *///?} elif >=1.21.11 {
        /*VertexConsumer lineBuilder = buffer.getBuffer(net.minecraft.client.renderer.RenderTypes.lines());

        double d0 = camera.position().x();
        double d1 = camera.position().y();
        double d2 = camera.position().z();
        *///?} elif >=1.21.9 {
        VertexConsumer lineBuilder = buffer.getBuffer(RenderType.lines());

        double d0 = camera.position().x();
        double d1 = camera.position().y();
        double d2 = camera.position().z();
        //?} else {
        /*VertexConsumer lineBuilder = buffer.getBuffer(RenderType.LINES);

        double d0 = player.xOld + (player.getX() - player.xOld) * partialTicks;
        double d1 = player.yOld + player.getEyeHeight() + (player.getY() - player.yOld) * partialTicks;
        double d2 = player.zOld + (player.getZ() - player.zOld) * partialTicks;
        *///?}

        for(BlockPos block : blocks) {
            AABB aabb = new AABB(block).move(-d0, -d1, -d2);
            //? if >=26.2 {
            /*ms.pushPose();
            ms.translate(block.getX() - d0, block.getY() - d1, block.getZ() - d2);
            buffer.submitShapeOutline(ms, net.minecraft.world.phys.shapes.Shapes.block(),
                    net.minecraft.client.renderer.RenderTypes.lines(),
                    net.minecraft.util.ARGB.colorFromFloat(0.4F, colorR, colorG, colorB), 2.0F, false);
            ms.popPose();
            *///?} elif >=1.21.11 {
            /*ShapeRenderer.renderShape(ms, lineBuilder, net.minecraft.world.phys.shapes.Shapes.create(aabb), 0, 0, 0,
                    net.minecraft.util.ARGB.colorFromFloat(0.4F, colorR, colorG, colorB), 1.0F);
            *///?} elif >=1.21.9 {
            ShapeRenderer.renderLineBox(ms.last(), lineBuilder, aabb, colorR, colorG, colorB, 0.4F);
            //?} elif >=1.21.2 {
            /*ShapeRenderer.renderLineBox(ms, lineBuilder, aabb, colorR, colorG, colorB, 0.4F);
            *///?} else {
            /*LevelRenderer.renderLineBox(ms, lineBuilder, aabb, colorR, colorG, colorB, 0.4F);
            *///?}
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
