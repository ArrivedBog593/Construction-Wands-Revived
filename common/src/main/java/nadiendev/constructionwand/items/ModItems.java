package nadiendev.constructionwand.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import nadiendev.constructionwand.registry.Registrar;
import nadiendev.constructionwand.registry.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.items.containeritems.ItemVoidSack;
import nadiendev.constructionwand.items.core.ItemCoreAngel;
import nadiendev.constructionwand.items.core.ItemCoreDestruction;
import nadiendev.constructionwand.items.core.ItemCoreExchange;
import nadiendev.constructionwand.items.wand.ItemWandBasic;
import nadiendev.constructionwand.items.wand.ItemWandInfinity;

public class ModItems
{
    public static final Registrar<Item> ITEMS = new Registrar<>(BuiltInRegistries.ITEM);

    public static final RegistryObject<Item> WAND_WOOD      = ITEMS.register("wood_wand",      () -> new ItemWandBasic(new Item.Properties().stacksTo(1), Tiers.WOOD));
    public static final RegistryObject<Item> WAND_STONE     = ITEMS.register("stone_wand",     () -> new ItemWandBasic(new Item.Properties().stacksTo(1), Tiers.STONE));
    public static final RegistryObject<Item> WAND_IRON      = ITEMS.register("iron_wand",      () -> new ItemWandBasic(new Item.Properties().stacksTo(1), Tiers.IRON));
    public static final RegistryObject<Item> WAND_GOLD      = ITEMS.register("gold_wand",      () -> new ItemWandBasic(new Item.Properties().stacksTo(1), Tiers.GOLD));
    public static final RegistryObject<Item> WAND_DIAMOND   = ITEMS.register("diamond_wand",   () -> new ItemWandBasic(new Item.Properties().stacksTo(1), Tiers.DIAMOND));
    public static final RegistryObject<Item> WAND_NETHERITE = ITEMS.register("netherite_wand", () -> new ItemWandBasic(new Item.Properties().stacksTo(1).fireResistant(), Tiers.NETHERITE));
    public static final RegistryObject<Item> WAND_INFINITY  = ITEMS.register("infinity_wand",  () -> new ItemWandInfinity(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CORE_ANGEL       = ITEMS.register("core_angel",       () -> new ItemCoreAngel(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_DESTRUCTION = ITEMS.register("core_destruction", () -> new ItemCoreDestruction(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_EXCHANGE    = ITEMS.register("core_exchange",    () -> new ItemCoreExchange(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> VOID_SACK = ITEMS.register("void_sack",
            () -> new ItemVoidSack(new Item.Properties().stacksTo(1).fireResistant(), 0));

    @SuppressWarnings("unchecked")
    public static final RegistryObject<Item>[] WANDS = new RegistryObject[]{
            WAND_WOOD, WAND_STONE, WAND_IRON, WAND_GOLD, WAND_DIAMOND, WAND_NETHERITE, WAND_INFINITY};

    @SuppressWarnings("unchecked")
    public static final RegistryObject<Item>[] CORES = new RegistryObject[]{
            CORE_ANGEL, CORE_DESTRUCTION, CORE_EXCHANGE};

    @SuppressWarnings("unchecked")
    public static final RegistryObject<Item>[] CONTAINERITEMS = new RegistryObject[]{
            VOID_SACK};

    /** Forces the class to load, which declares every item. */
    public static void init() {
    }
}
