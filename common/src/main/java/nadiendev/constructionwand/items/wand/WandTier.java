package nadiendev.constructionwand.items.wand;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

/**
 * The vanilla tool tier a basic wand is made of. Vanilla replaced {@code Tiers} with {@code ToolMaterial} in
 * 1.21.2, so everything that touches either goes through this enum.
 */
public enum WandTier {
    WOOD, STONE, COPPER, IRON, GOLD, DIAMOND, NETHERITE;


    private ToolMaterial material() {
        return switch (this) {
            case WOOD -> ToolMaterial.WOOD;
            case STONE -> ToolMaterial.STONE;
            case COPPER -> ToolMaterial.COPPER;
            case IRON -> ToolMaterial.IRON;
            case GOLD -> ToolMaterial.GOLD;
            case DIAMOND -> ToolMaterial.DIAMOND;
            case NETHERITE -> ToolMaterial.NETHERITE;
        };
    }

    /** Durability of the vanilla tier. */
    public int durability() {
        return material().durability();
    }

    /** How well the wand takes enchantments, like the tools of the tier. */
    public int enchantability() {
        return material().enchantmentValue();
    }

    /** Applies durability, enchantability and the repair ingredient to the item properties. */
    public Item.Properties applyTo(Item.Properties properties) {
        return properties.durability(durability()).enchantable(enchantability()).repairable(material().repairItems());
    }
}
