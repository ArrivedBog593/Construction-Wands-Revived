package nadiendev.constructionwand.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.world.phys.BlockHitResult;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.client.RenderBlockPreview;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric dropped its block outline event in 1.21.9, so the placement preview replaces the vanilla outline here. */
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Inject(method = "renderBlockOutline", at = @At("HEAD"), cancellable = true)
    private void constructionwand$renderPreview(MultiBufferSource.BufferSource buffer, PoseStack poseStack,
                                                boolean translucentPass, LevelRenderState levelRenderState,
                                                CallbackInfo ci) {
        RenderBlockPreview preview = ConstructionWand.instance.renderBlockPreview;
        if (preview == null || levelRenderState.blockOutlineRenderState == null) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (!(minecraft.hitResult instanceof BlockHitResult hit)) return;

        boolean handled = translucentPass
                ? preview.blockOutlineHandled
                : preview.renderBlockHighlight(hit, minecraft.gameRenderer.getMainCamera(), poseStack, buffer, 0);
        if (handled) ci.cancel();
    }
}
