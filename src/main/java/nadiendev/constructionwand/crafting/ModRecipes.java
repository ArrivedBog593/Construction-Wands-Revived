package nadiendev.constructionwand.crafting;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
//? if >=1.21.2 {
/*import net.minecraft.world.item.crafting.CustomRecipe;
*///?} else {
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
//?}
import nadiendev.constructionwand.registry.Registrar;
import nadiendev.constructionwand.registry.RegistryObject;

import java.util.function.Supplier;

public class ModRecipes
{

    public static final Registrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = new Registrar<>(BuiltInRegistries.RECIPE_SERIALIZER);

    //? if >=1.21.2 {
    /*public static final Supplier<CustomRecipe.Serializer<RecipeWandUpgrade>> WAND_UPGRADE = RECIPE_SERIALIZERS.register("wand_upgrade", () -> new CustomRecipe.Serializer<>(RecipeWandUpgrade::new));
    *///?} else {
    public static final Supplier<SimpleCraftingRecipeSerializer<RecipeWandUpgrade>> WAND_UPGRADE = RECIPE_SERIALIZERS.register("wand_upgrade", () -> new SimpleCraftingRecipeSerializer<>(RecipeWandUpgrade::new));
    //?}

    public static void init() {
    }
}
