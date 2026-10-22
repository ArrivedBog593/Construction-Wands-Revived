package nadiendev.constructionwand.items.wand;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;

/**
 * The vanilla tool tier a basic wand is made of. Vanilla replaced {@code Tiers} with {@code ToolMaterial} in
 * 1.21.2, so everything that touches either goes through this enum.
 */
public enum WandTier {
    WOOD, STONE, COPPER, IRON, GOLD, DIAMOND, NETHERITE;

    /** Same as the vanilla copper tools. */
    private static final int COPPER_DURABILITY = 190;
    private static final int COPPER_ENCHANTABILITY = 13;

    private Tiers material() {
        return switch (this) {
            case WOOD -> Tiers.WOOD;
            case STONE -> Tiers.STONE;
            case COPPER -> null; // vanilla has no copper tier before 1.21.9
            case IRON -> Tiers.IRON;
            case GOLD -> Tiers.GOLD;
            case DIAMOND -> Tiers.DIAMOND;
            case NETHERITE -> Tiers.NETHERITE;
        };
    }

    /** Durability of the vanilla tier. */
    public int durability() {
        if(this == COPPER) return COPPER_DURABILITY;
        return material().getUses();
    }

    /** Applies the durability to the item properties. */
    public Item.Properties applyTo(Item.Properties properties) {
        return properties.durability(durability());
    }

    /** How well the wand takes enchantments, like the tools of the tier. */
    public int enchantability() {
        if(this == COPPER) return COPPER_ENCHANTABILITY;
        return material().getEnchantmentValue();
    }

    /** Whether the stack can repair items of this tier. */
    public boolean isRepairItem(ItemStack repair) {
        if(this == COPPER) return repair.is(Items.COPPER_INGOT);
        return material().getRepairIngredient().test(repair);
    }
}
