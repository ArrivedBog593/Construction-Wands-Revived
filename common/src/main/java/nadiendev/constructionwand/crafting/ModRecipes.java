package nadiendev.constructionwand.crafting;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import nadiendev.constructionwand.registry.Registrar;
import nadiendev.constructionwand.registry.RegistryObject;

import java.util.function.Supplier;

public class ModRecipes
{

    public static final Registrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = new Registrar<>(BuiltInRegistries.RECIPE_SERIALIZER);

    public static final Supplier<CustomRecipe.Serializer<RecipeWandUpgrade>> WAND_UPGRADE = RECIPE_SERIALIZERS.register("wand_upgrade", () -> new CustomRecipe.Serializer<>(RecipeWandUpgrade::new));

    public static void init() {
    }
}
