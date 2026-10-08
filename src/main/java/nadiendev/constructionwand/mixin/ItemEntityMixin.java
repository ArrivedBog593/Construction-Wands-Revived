//? if fabric {
/*package nadiendev.constructionwand.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import nadiendev.constructionwand.events.VoidSackPickupHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/^* Fabric has no pickup event, so the void sack hooks the moment a player touches an item entity. ^/
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void constructionwand$voidSackPickup(Player player, CallbackInfo ci) {
        if (VoidSackPickupHandler.onItemPickup(player, (ItemEntity) (Object) this)) {
            ci.cancel();
        }
    }
}
*///?}
