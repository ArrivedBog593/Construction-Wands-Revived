package nadiendev.constructionwand.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import nadiendev.constructionwand.items.wand.ItemWand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Enchantments are tied to hard coded categories before 1.21, and Fabric has no hook for an item to accept one.
 * The wands take Silk Touch (Fortune stays out), so the check of the enchanting table and the anvil is widened here.
 * On Forge {@code ItemWand#canApplyAtEnchantingTable} does the same.
 */
@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void constructionwand$silkTouchOnWands(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == Enchantments.SILK_TOUCH && stack.getItem() instanceof ItemWand) {
            cir.setReturnValue(true);
        }
    }
}
