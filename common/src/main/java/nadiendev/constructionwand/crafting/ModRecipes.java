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

    public static final Supplier<RecipeSerializer<RecipeWandUpgrade>> WAND_UPGRADE = RECIPE_SERIALIZERS.register("wand_upgrade",
            () -> new RecipeSerializer<>(com.mojang.serialization.MapCodec.unit(RecipeWandUpgrade::new),
                    net.minecraft.network.codec.StreamCodec.unit(new RecipeWandUpgrade())));

    public static void init() {
    }
}
