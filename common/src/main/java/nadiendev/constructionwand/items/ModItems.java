package nadiendev.constructionwand.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import nadiendev.constructionwand.registry.Registrar;
import nadiendev.constructionwand.registry.RegistryObject;

import java.util.function.Function;
import net.minecraft.core.registries.BuiltInRegistries;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.items.containeritems.ItemVoidSack;
import nadiendev.constructionwand.items.core.ItemCoreAngel;
import nadiendev.constructionwand.items.core.ItemCoreDestruction;
import nadiendev.constructionwand.items.core.ItemCoreExchange;
import nadiendev.constructionwand.items.wand.ItemWandBasic;
import nadiendev.constructionwand.items.wand.WandTier;
import nadiendev.constructionwand.items.wand.ItemWandInfinity;

public class ModItems
{
    public static final Registrar<Item> ITEMS = new Registrar<>(BuiltInRegistries.ITEM);

    /** Registers an item whose properties are created for it; from 1.21.2 on they must carry the item's own id. */
    private static RegistryObject<Item> item(String name, Function<Item.Properties, Item> factory) {
        return ITEMS.register(name, () -> {
            Item.Properties properties = new Item.Properties();
            //? if >=1.21.2
            properties.setId(ResourceKey.create(Registries.ITEM, ConstructionWand.loc(name)));
            return factory.apply(properties);
        });
    }

    public static final RegistryObject<Item> WAND_WOOD      = item("wood_wand", props -> new ItemWandBasic(props.stacksTo(1), WandTier.WOOD));
    public static final RegistryObject<Item> WAND_STONE     = item("stone_wand", props -> new ItemWandBasic(props.stacksTo(1), WandTier.STONE));
    public static final RegistryObject<Item> WAND_IRON      = item("iron_wand", props -> new ItemWandBasic(props.stacksTo(1), WandTier.IRON));
    public static final RegistryObject<Item> WAND_GOLD      = item("gold_wand", props -> new ItemWandBasic(props.stacksTo(1), WandTier.GOLD));
    public static final RegistryObject<Item> WAND_DIAMOND   = item("diamond_wand", props -> new ItemWandBasic(props.stacksTo(1), WandTier.DIAMOND));
    public static final RegistryObject<Item> WAND_NETHERITE = item("netherite_wand", props -> new ItemWandBasic(props.stacksTo(1).fireResistant(), WandTier.NETHERITE));
    public static final RegistryObject<Item> WAND_INFINITY  = item("infinity_wand", props -> new ItemWandInfinity(props.stacksTo(1)));

    public static final RegistryObject<Item> CORE_ANGEL       = item("core_angel", props -> new ItemCoreAngel(props.stacksTo(1)));
    public static final RegistryObject<Item> CORE_DESTRUCTION = item("core_destruction", props -> new ItemCoreDestruction(props.stacksTo(1)));
    public static final RegistryObject<Item> CORE_EXCHANGE    = item("core_exchange", props -> new ItemCoreExchange(props.stacksTo(1)));

    public static final RegistryObject<Item> VOID_SACK = item("void_sack",
            props -> new ItemVoidSack(props.stacksTo(1).fireResistant(), 0));

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
