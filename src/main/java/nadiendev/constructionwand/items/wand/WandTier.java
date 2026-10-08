package nadiendev.constructionwand.items.wand;

import net.minecraft.world.item.Item;
//? if >=1.21.2 {
/*import net.minecraft.world.item.ToolMaterial;
*///?} else {
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
//?}

/**
 * The vanilla tool tier a basic wand is made of. Vanilla replaced {@code Tiers} with {@code ToolMaterial} in
 * 1.21.2, so everything that touches either goes through this enum.
 */
public enum WandTier {
    WOOD, STONE, IRON, GOLD, DIAMOND, NETHERITE;

    //? if >=1.21.2 {
    /*private ToolMaterial material() {
        return switch (this) {
            case WOOD -> ToolMaterial.WOOD;
            case STONE -> ToolMaterial.STONE;
            case IRON -> ToolMaterial.IRON;
            case GOLD -> ToolMaterial.GOLD;
            case DIAMOND -> ToolMaterial.DIAMOND;
            case NETHERITE -> ToolMaterial.NETHERITE;
        };
    }

    /^* Durability of the vanilla tier. ^/
    public int durability() {
        return material().durability();
    }

    /^* Applies durability and the repair ingredient to the item properties. ^/
    public Item.Properties applyTo(Item.Properties properties) {
        return properties.durability(durability()).repairable(material().repairItems());
    }
    *///?} else {
    private Tiers material() {
        return switch (this) {
            case WOOD -> Tiers.WOOD;
            case STONE -> Tiers.STONE;
            case IRON -> Tiers.IRON;
            case GOLD -> Tiers.GOLD;
            case DIAMOND -> Tiers.DIAMOND;
            case NETHERITE -> Tiers.NETHERITE;
        };
    }

    /** Durability of the vanilla tier. */
    public int durability() {
        return material().getUses();
    }

    /** Applies the durability to the item properties. */
    public Item.Properties applyTo(Item.Properties properties) {
        return properties.durability(durability());
    }

    /** Whether the stack can repair items of this tier. */
    public boolean isRepairItem(ItemStack repair) {
        return material().getRepairIngredient().test(repair);
    }
    //?}
}
