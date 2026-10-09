package nadiendev.constructionwand.registry;

import nadiendev.constructionwand.basics.ModStats;
import nadiendev.constructionwand.crafting.ModRecipes;
import nadiendev.constructionwand.creative.ModCreativeTabs;
import nadiendev.constructionwand.items.ModItems;

import java.util.List;

/** All registrars of the mod, in registration order. Loaders iterate this list. */
public final class ModRegistries
{
    private ModRegistries() {
    }

    public static List<Registrar<?>> all() {
        return List.of(
                ModItems.ITEMS,
                ModRecipes.RECIPE_SERIALIZERS,
                ModMenuTypes.MENU_TYPES,
                ModStats.CUSTOM_STATS,
                ModCreativeTabs.CREATIVE_TABS);
    }
}
