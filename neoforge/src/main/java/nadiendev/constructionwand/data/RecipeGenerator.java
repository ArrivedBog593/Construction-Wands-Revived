package nadiendev.constructionwand.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.crafting.ModRecipes;
import nadiendev.constructionwand.crafting.RecipeWandUpgrade;
import nadiendev.constructionwand.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends RecipeProvider {
    private final HolderGetter<Item> registries = items;

    public RecipeGenerator(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
    }

    @Override
    protected void buildRecipes() {
        wandRecipe(ModItems.WAND_WOOD.get(), Inp.fromItems(registries, "wood_planks", Items.CHERRY_PLANKS, Items.BIRCH_PLANKS));
        wandRecipe(ModItems.WAND_STONE.get(), Inp.fromTag(registries, ItemTags.STONE_TOOL_MATERIALS));
        wandRecipe(ModItems.WAND_COPPER.get(), Inp.fromTag(registries, Tags.Items.INGOTS_COPPER));
        wandRecipe(ModItems.WAND_IRON.get(), Inp.fromTag(registries, Tags.Items.INGOTS_IRON));
        wandRecipe(ModItems.WAND_GOLD.get(), Inp.fromTag(registries, Tags.Items.INGOTS_GOLD));
        wandRecipe(ModItems.WAND_DIAMOND.get(), Inp.fromTag(registries, Tags.Items.GEMS_DIAMOND));
        wandRecipe(ModItems.WAND_NETHERITE.get(), Inp.fromTag(registries, Tags.Items.INGOTS_NETHERITE));
        wandRecipe(ModItems.WAND_INFINITY.get(), Inp.fromTag(registries, Tags.Items.NETHER_STARS));

        coreRecipe(ModItems.CORE_ANGEL.get(), Inp.fromTag(registries, Tags.Items.FEATHERS), Inp.fromTag(registries, Tags.Items.INGOTS_GOLD));
        coreRecipe(ModItems.CORE_DESTRUCTION.get(), Inp.fromTag(registries, Tags.Items.STORAGE_BLOCKS_DIAMOND), Inp.fromItem(registries, Items.DIAMOND_PICKAXE));
        coreRecipe(ModItems.CORE_EXCHANGE.get(), Inp.fromItem(registries, Items.NETHER_STAR), Inp.fromTag(registries, Tags.Items.STORAGE_BLOCKS_NETHERITE));

        energyUpgradeRecipe();
        voidSackRecipe();

        specialRecipe();
    }

    private void wandRecipe(ItemLike wand, Inp material) {
        shaped(RecipeCategory.TOOLS, wand)
                .define('X', material.ingredient)
                .define('#', Tags.Items.RODS_WOODEN)
                .pattern("  X")
                .pattern(" # ")
                .pattern("#  ")
                .unlockedBy("has_item", inventoryTrigger(material.predicate))
                .save(output);
    }

    private void coreRecipe(ItemLike core, Inp item1, Inp item2) {
        shaped(RecipeCategory.MISC, core)
                .define('O', item1.ingredient)
                .define('X', item2.ingredient)
                .define('#', Tags.Items.GLASS_PANES)
                .pattern(" #X")
                .pattern("#O#")
                .pattern("X# ")
                .unlockedBy("has_item", inventoryTrigger(item1.predicate))
                .save(output);
    }

    private void energyUpgradeRecipe() {
        Inp copper = Inp.fromTag(registries, Tags.Items.INGOTS_COPPER);
        shaped(RecipeCategory.MISC, ModItems.ENERGY_UPGRADE.get())
                .define('C', copper.ingredient)
                .define('R', Items.REDSTONE_BLOCK)
                .pattern("CCC")
                .pattern("CRC")
                .pattern("CCC")
                .unlockedBy("has_item", inventoryTrigger(copper.predicate))
                .save(output);
    }

    private void voidSackRecipe() {
        Inp coreDestruction = Inp.fromItem(registries, ModItems.CORE_DESTRUCTION.get());
        shaped(RecipeCategory.MISC, ModItems.VOID_SACK.get())
                .define('C', coreDestruction.ingredient)
                .define('E', Items.DIAMOND)
                .define('O', Items.NETHERITE_INGOT)
                .define('D', Items.EMERALD)
                .pattern("EDE")
                .pattern("OCO")
                .pattern("EDE")
                .unlockedBy("has_core_destruction", inventoryTrigger(coreDestruction.predicate))
                .save(output);
    }

    private void specialRecipe() {
        Identifier name = BuiltInRegistries.RECIPE_SERIALIZER.getKey(ModRecipes.WAND_UPGRADE.get());
        SpecialRecipeBuilder.special(RecipeWandUpgrade::new)
                .save(output, ConstructionWand.loc("dynamic/" + name.getPath()).toString());
    }
}
