//? if fabric {
package nadiendev.constructionwand.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import nadiendev.constructionwand.events.VoidSackDropHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Fabric has no cancellable entity-join event, so the void sack hooks the level's entity add path. */
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void constructionwand$voidSackDrop(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (VoidSackDropHandler.onEntityJoinLevel((ServerLevel) (Object) this, entity)) {
            cir.setReturnValue(false);
        }
    }
}
//?}
