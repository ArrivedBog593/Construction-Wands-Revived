package nadiendev.constructionwand.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import nadiendev.constructionwand.client.ClientEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric has no mouse scroll event: lets the wand consume Sneak+(OPT)+Scroll before the hotbar sees it. */
@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void constructionwand$onScroll(long window, double xOffset, double yOffset, CallbackInfo ci) {
        if (window == minecraft.getWindow().getWindow() && minecraft.screen == null && ClientEvents.onMouseScroll(yOffset)) {
            ci.cancel();
        }
    }
}
