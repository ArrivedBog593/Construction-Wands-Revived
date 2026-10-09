package nadiendev.constructionwand.platform;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

/**
 * Side neutral item model predicate. Vanilla's own interface lives in client-only code, which must not appear
 * in signatures that a dedicated server loads.
 */
@FunctionalInterface
public interface ItemPropertyFunction {
    float call(ItemStack stack, @Nullable Level level, @Nullable LivingEntity entity, int seed);
}
