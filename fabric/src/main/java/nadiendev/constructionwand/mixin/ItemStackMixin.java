package nadiendev.constructionwand.mixin;

import net.minecraft.world.item.ItemStack;
import nadiendev.constructionwand.items.wand.ItemWandBasic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Fabric has no per stack durability hook, so the configurable wand durability redirects ItemStack#getMaxDamage. */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
    private void constructionwand$configuredDurability(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof ItemWandBasic wand) {
            cir.setReturnValue(wand.getMaxDamage(stack));
        }
    }
}
