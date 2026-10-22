package nadiendev.constructionwand.items;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import nadiendev.constructionwand.items.containeritems.ItemVoidSack;
import nadiendev.constructionwand.items.core.ItemCoreAngel;
import nadiendev.constructionwand.items.core.ItemCoreDestruction;
import nadiendev.constructionwand.items.core.ItemCoreExchange;
import nadiendev.constructionwand.items.upgrade.ItemEnergyUpgrade;
import nadiendev.constructionwand.items.wand.ItemWandInfinity;
import nadiendev.constructionwand.items.wand.WandTiers;
import nadiendev.constructionwand.platform.Services;
import nadiendev.constructionwand.registry.Registrar;
import nadiendev.constructionwand.registry.RegistryObject;

public class ModItems
{
    public static final Registrar<Item> ITEMS = new Registrar<>(BuiltInRegistries.ITEM);

    // Wands
    public static final RegistryObject<Item> WAND_WOOD = ITEMS.register("wood_wand", () -> Services.PLATFORM.newBasicWand(propWand(), Tiers.WOOD));
    public static final RegistryObject<Item> WAND_STONE = ITEMS.register("stone_wand", () -> Services.PLATFORM.newBasicWand(propWand(), Tiers.STONE));
    public static final RegistryObject<Item> WAND_COPPER = ITEMS.register("copper_wand", () -> Services.PLATFORM.newBasicWand(propWand(), WandTiers.COPPER));
    public static final RegistryObject<Item> WAND_IRON = ITEMS.register("iron_wand", () -> Services.PLATFORM.newBasicWand(propWand(), Tiers.IRON));
    public static final RegistryObject<Item> WAND_GOLD = ITEMS.register("gold_wand", () -> Services.PLATFORM.newBasicWand(propWand(), Tiers.GOLD));
    public static final RegistryObject<Item> WAND_DIAMOND = ITEMS.register("diamond_wand", () -> Services.PLATFORM.newBasicWand(propWand(), Tiers.DIAMOND));
    public static final RegistryObject<Item> WAND_NETHERITE = ITEMS.register("netherite_wand", () -> Services.PLATFORM.newBasicWand(propWand().fireResistant(), Tiers.NETHERITE));
    public static final RegistryObject<Item> WAND_INFINITY = ITEMS.register("infinity_wand", () -> new ItemWandInfinity(propWand()));

    // Cores
    public static final RegistryObject<Item> CORE_ANGEL = ITEMS.register("core_angel", () -> new ItemCoreAngel(propUpgrade()));
    public static final RegistryObject<Item> CORE_DESTRUCTION = ITEMS.register("core_destruction", () -> new ItemCoreDestruction(propUpgrade()));
    public static final RegistryObject<Item> CORE_EXCHANGE = ITEMS.register("core_exchange", () -> new ItemCoreExchange(propUpgrade()));

    // Upgrades
    public static final RegistryObject<Item> ENERGY_UPGRADE = ITEMS.register("energy_upgrade", () -> new ItemEnergyUpgrade(propUpgrade()));

    public static final RegistryObject<Item> VOID_SACK = ITEMS.register("void_sack",
            () -> new ItemVoidSack(new Item.Properties().stacksTo(1).fireResistant(), 0));

    // Collections
    @SuppressWarnings("unchecked")
    public static final RegistryObject<Item>[] WANDS = new RegistryObject[] {WAND_WOOD, WAND_STONE, WAND_COPPER, WAND_IRON, WAND_GOLD, WAND_DIAMOND, WAND_NETHERITE, WAND_INFINITY};
    @SuppressWarnings("unchecked")
    public static final RegistryObject<Item>[] CORES = new RegistryObject[] {CORE_ANGEL, CORE_DESTRUCTION, CORE_EXCHANGE};
    @SuppressWarnings("unchecked")
    public static final RegistryObject<Item>[] CONTAINERITEMS = new RegistryObject[] {VOID_SACK};

    public static Item.Properties propWand() {
        return new Item.Properties();
    }

    private static Item.Properties propUpgrade() {
        return new Item.Properties().stacksTo(1);
    }
}
