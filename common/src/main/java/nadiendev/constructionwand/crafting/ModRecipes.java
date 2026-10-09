package nadiendev.constructionwand.crafting;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import nadiendev.constructionwand.registry.Registrar;
import nadiendev.constructionwand.registry.RegistryObject;

public class ModRecipes
{
    public static final Registrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = new Registrar<>(BuiltInRegistries.RECIPE_SERIALIZER);

    public static final RegistryObject<SimpleCraftingRecipeSerializer<RecipeWandUpgrade>> WAND_UPGRADE =
            RECIPE_SERIALIZERS.register("wand_upgrade", () -> new SimpleCraftingRecipeSerializer<>(RecipeWandUpgrade::new));
}
