//? if fabric {
package nadiendev.constructionwand.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.HitResult;
import nadiendev.constructionwand.client.ClientEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Fabric has no "left click on nothing" event, so the wand hooks the start of the attack action. */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    private int missTime;

    @Shadow
    public HitResult hitResult;

    @Shadow
    public LocalPlayer player;

    @Inject(method = "startAttack", at = @At("HEAD"))
    private void constructionwand$leftClickEmpty(CallbackInfoReturnable<Boolean> cir) {
        if (missTime > 0 || player == null || player.isHandsBusy()) return;
        if (hitResult == null || hitResult.getType() != HitResult.Type.MISS) return;
        ClientEvents.onLeftClickEmpty(player, player.getMainHandItem());
    }
}
//?}
