package nadiendev.constructionwand.items.wand;


public class ItemWandInfinity extends ItemWand
{
    public ItemWandInfinity(Properties properties) {
        super(configure(properties));
    }

    private static Properties configure(Properties properties) {
        properties = properties.stacksTo(1).fireResistant();
        // Enchantable like the other wands (Silk Touch); before 1.21.2 this is ItemWand#isEnchantable
        properties = properties.enchantable(15);
        return properties;
    }
}
