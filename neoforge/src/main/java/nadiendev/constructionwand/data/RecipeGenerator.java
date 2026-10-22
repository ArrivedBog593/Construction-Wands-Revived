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
import net.minecraft.resources.ResourceLocation;
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
    public RecipeGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output, HolderLookup.Provider provider) {
        wandRecipe(output, ModItems.WAND_WOOD.get(), Inp.fromItems(provider, "wood_planks", Items.CHERRY_PLANKS, Items.BIRCH_PLANKS));
        wandRecipe(output, ModItems.WAND_STONE.get(), Inp.fromTag(provider, ItemTags.STONE_TOOL_MATERIALS));
        wandRecipe(output, ModItems.WAND_COPPER.get(), Inp.fromTag(provider, Tags.Items.INGOTS_COPPER));
        wandRecipe(output, ModItems.WAND_IRON.get(), Inp.fromTag(provider, Tags.Items.INGOTS_IRON));
        // WAND_GOLD had no recipe at all before this change; the item existed but couldn't be crafted.
        wandRecipe(output, ModItems.WAND_GOLD.get(), Inp.fromTag(provider, Tags.Items.INGOTS_GOLD));
        wandRecipe(output, ModItems.WAND_DIAMOND.get(), Inp.fromTag(provider, Tags.Items.GEMS_DIAMOND));
        wandRecipe(output, ModItems.WAND_NETHERITE.get(), Inp.fromTag(provider, Tags.Items.INGOTS_NETHERITE));
        wandRecipe(output, ModItems.WAND_INFINITY.get(), Inp.fromTag(provider, Tags.Items.NETHER_STARS));

        coreRecipe(output, ModItems.CORE_ANGEL.get(), Inp.fromTag(provider, Tags.Items.FEATHERS), Inp.fromTag(provider, Tags.Items.INGOTS_GOLD));
        coreRecipe(output, ModItems.CORE_DESTRUCTION.get(), Inp.fromTag(provider, Tags.Items.STORAGE_BLOCKS_DIAMOND), Inp.fromItem(provider, Items.DIAMOND_PICKAXE));
        coreRecipe(output, ModItems.CORE_EXCHANGE.get(), Inp.fromItem(provider, Items.NETHER_STAR), Inp.fromTag(provider, Tags.Items.STORAGE_BLOCKS_NETHERITE));

        energyUpgradeRecipe(output, provider);
        voidSackRecipe(output, provider);

        specialRecipe(output, ModRecipes.WAND_UPGRADE.get());
    }

    private void wandRecipe(RecipeOutput output, ItemLike wand, Inp material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, wand)
                .define('X', material.ingredient)
                .define('#', Tags.Items.RODS_WOODEN)
                .pattern("  X")
                .pattern(" # ")
                .pattern("#  ")
                .unlockedBy("has_item", inventoryTrigger(material.predicate))
                .save(output);
    }

    private void coreRecipe(RecipeOutput output, ItemLike core, Inp item1, Inp item2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, core)
                .define('O', item1.ingredient)
                .define('X', item2.ingredient)
                .define('#', Tags.Items.GLASS_PANES)
                .pattern(" #X")
                .pattern("#O#")
                .pattern("X# ")
                .unlockedBy("has_item", inventoryTrigger(item1.predicate))
                .save(output);
    }

    private void energyUpgradeRecipe(RecipeOutput output, HolderLookup.Provider provider) {
        Inp copper = Inp.fromTag(provider, Tags.Items.INGOTS_COPPER);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ENERGY_UPGRADE.get())
                .define('C', copper.ingredient)
                .define('R', Items.REDSTONE_BLOCK)
                .pattern("CCC")
                .pattern("CRC")
                .pattern("CCC")
                .unlockedBy("has_item", inventoryTrigger(copper.predicate))
                .save(output);
    }

    private void voidSackRecipe(RecipeOutput output, HolderLookup.Provider provider) {
        Inp coreDestruction = Inp.fromItem(provider, ModItems.CORE_DESTRUCTION.get());
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VOID_SACK.get())
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

    public static void register(net.minecraft.data.DataGenerator generator, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        generator.addProvider(true, new RecipeGenerator(packOutput, lookupProvider));
    }

    private void specialRecipe(RecipeOutput output, net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<?> serializer) {
        ResourceLocation name = BuiltInRegistries.RECIPE_SERIALIZER.getKey(serializer);
        SpecialRecipeBuilder.special(RecipeWandUpgrade::new).save(output, ConstructionWand.loc("dynamic/" + name.getPath()).toString());
    }
}
