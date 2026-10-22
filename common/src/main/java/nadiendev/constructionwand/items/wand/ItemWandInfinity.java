package nadiendev.constructionwand.items.wand;


public class ItemWandInfinity extends ItemWand
{
    public ItemWandInfinity(Properties properties) {
        // Not damageable, so it needs the enchantable component to take enchantments (Silk Touch)
        super(properties.stacksTo(1).fireResistant().enchantable(15));
    }
}
