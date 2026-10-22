package nadiendev.constructionwand.items.wand;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;

/**
 * The vanilla tool tier a basic wand is made of. Vanilla replaced {@code Tiers} with {@code ToolMaterial} in
 * 1.21.2, so everything that touches either goes through this enum.
 */
public enum WandTier {
    WOOD, STONE, COPPER, IRON, GOLD, DIAMOND, NETHERITE;

    /** Same as the vanilla copper tools (vanilla has no copper tier before 1.21.9). */
    private static final int COPPER_DURABILITY = 190;
    private static final int COPPER_ENCHANTABILITY = 13;

    private ToolMaterial material() {
        return switch (this) {
            case WOOD -> ToolMaterial.WOOD;
            case STONE -> ToolMaterial.STONE;
            case COPPER -> null; // vanilla has no copper tool material before 1.21.9
            case IRON -> ToolMaterial.IRON;
            case GOLD -> ToolMaterial.GOLD;
            case DIAMOND -> ToolMaterial.DIAMOND;
            case NETHERITE -> ToolMaterial.NETHERITE;
        };
    }

    /** Durability of the vanilla tier. */
    public int durability() {
        if(this == COPPER) return COPPER_DURABILITY;
        return material().durability();
    }

    /** How well the wand takes enchantments, like the tools of the tier. */
    public int enchantability() {
        if(this == COPPER) return COPPER_ENCHANTABILITY;
        return material().enchantmentValue();
    }

    /** Applies durability, enchantability and the repair ingredient to the item properties. */
    public Item.Properties applyTo(Item.Properties properties) {
        properties = properties.durability(durability()).enchantable(enchantability());
        if(this == COPPER) return properties.repairable(Items.COPPER_INGOT);
        return properties.repairable(material().repairItems());
    }
}
