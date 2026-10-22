package nadiendev.constructionwand.data;

import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.items.ModItems;
import nadiendev.constructionwand.registry.RegistryObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;
import java.util.function.Consumer;

public class WandAdvancementSubProvider extends AdvancementSubProvider
{
    public WandAdvancementSubProvider(BootstrapContext<Advancement> output) {
        super(output);
    }

    @Override
    public void generate() {
        build(output, output.lookup(Registries.ITEM));
    }

    private static void build(BootstrapContext<Advancement> consumer, HolderGetter<Item> items) {
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(rootDisplay(
                        ModItems.WAND_STONE.get(),
                        advancementPrefix("root.title"),
                        advancementPrefix("root.desc"),
                        Identifier.withDefaultNamespace("textures/block/oak_planks.png")))
                .addCriterion("wand", InventoryChangeTrigger.TriggerInstance.hasItems(
                        net.minecraft.advancements.predicates.ItemPredicate.Builder.item().of(items, ModItems.WAND_STONE.get()).build()))
                .save(consumer, ConstructionWand.loc("root").toString());

        onHasItem(consumer, ModItems.WAND_WOOD, AdvancementType.TASK, root);
        onHasItem(consumer, ModItems.WAND_STONE, AdvancementType.TASK, root);
        onHasItem(consumer, ModItems.WAND_COPPER, AdvancementType.TASK, root);
        onHasItem(consumer, ModItems.WAND_IRON, AdvancementType.TASK, root);
        onHasItem(consumer, ModItems.WAND_GOLD, AdvancementType.TASK, root);
        onHasItem(consumer, ModItems.WAND_DIAMOND, AdvancementType.TASK, root);
        onHasItem(consumer, ModItems.WAND_NETHERITE, AdvancementType.TASK, root);
        onHasItem(consumer, ModItems.WAND_INFINITY, AdvancementType.GOAL, root);
        onHasItem(consumer, ModItems.CORE_ANGEL, AdvancementType.TASK, root);
        onHasItem(consumer, ModItems.CORE_DESTRUCTION, AdvancementType.TASK, root);
        onHasItem(consumer, ModItems.CORE_EXCHANGE, AdvancementType.TASK, root);
        onHasItem(consumer, ModItems.ENERGY_UPGRADE, AdvancementType.TASK, root);
        onHasItem(consumer, ModItems.VOID_SACK, AdvancementType.TASK, root);
    }

    private static void onHasItem(BootstrapContext<Advancement> consumer, RegistryObject<Item> item,
                                  AdvancementType type, AdvancementHolder parent) {
        String path = item.getId().getPath();
        Advancement.Builder.advancement()
                .display(simpleDisplay(item.get(), path, type))
                .parent(parent)
                .addCriterion(path, InventoryChangeTrigger.TriggerInstance.hasItems(item.get()))
                .save(consumer, ConstructionWand.loc(path).toString());
    }

    private static DisplayInfo rootDisplay(ItemLike icon, String titleKey, String descKey, Identifier background) {
        return new DisplayInfo(iconStack(icon), Component.translatable(titleKey), Component.translatable(descKey),
                AdvancementCompat.background(background), AdvancementType.TASK, false, false, false);
    }

    private static DisplayInfo simpleDisplay(ItemLike icon, String name, AdvancementType type) {
        return new DisplayInfo(iconStack(icon), Component.translatable(advancementPrefix(name + ".title")),
                Component.translatable(advancementPrefix(name + ".desc")), Optional.empty(), type, true, true, false);
    }

    private static net.minecraft.world.item.ItemStackTemplate iconStack(ItemLike icon) {
        return new net.minecraft.world.item.ItemStackTemplate(icon.asItem());
    }

    private static String advancementPrefix(String name) {
        return "advancement." + ConstructionWand.MODID + "." + name;
    }
}
