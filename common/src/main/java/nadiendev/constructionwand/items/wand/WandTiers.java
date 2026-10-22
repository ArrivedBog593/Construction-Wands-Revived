package nadiendev.constructionwand.items.wand;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Vanilla has no copper tier before 1.21.9, so the values of the copper wand are hard coded here (same as the
 * copper tools of later versions). The other wands use the vanilla {@code Tiers}.
 */
public final class WandTiers
{
    private WandTiers() {}

    public static final Tier COPPER = new Tier()
    {
        @Override
        public int getUses() {
            return 190;
        }

        @Override
        public float getSpeed() {
            return 5.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 1.0F;
        }

        @Override
        public int getLevel() {
            return 1;
        }

        @Override
        public int getEnchantmentValue() {
            return 13;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(Items.COPPER_INGOT);
        }
    };
}
