//? if neoforge {
package nadiendev.constructionwand.data;

import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

/** An ingredient together with the advancement predicate that unlocks the recipe it is used in. */
public class Inp
{
    public final String name;
    public final Ingredient ingredient;
    public final ItemPredicate predicate;

    public Inp(String name, Ingredient ingredient, ItemPredicate predicate) {
        this.name = name;
        this.ingredient = ingredient;
        this.predicate = predicate;
    }

    public static Inp fromItem(HolderLookup.Provider registries, ItemLike in) {
        return new Inp(BuiltInRegistries.ITEM.getKey(in.asItem()).getPath(), Ingredient.of(in), predicate(registries, in));
    }

    public static Inp fromItems(HolderLookup.Provider registries, String name, ItemLike... in) {
        return new Inp(name, Ingredient.of(in), predicate(registries, in));
    }

    public static Inp fromTag(HolderLookup.Provider registries, TagKey<Item> in) {
        //? if >=1.21.2 {
        Ingredient ingredient = Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(in));
        return new Inp(in.location().getPath(), ingredient,
                ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), in).build());
        //?} else {
        /*return new Inp(in.location().getPath(), Ingredient.of(in), ItemPredicate.Builder.item().of(in).build());
        *///?}
    }

    //? if >=26.3 {
    /*public static Inp fromItem(HolderGetter<Item> items, ItemLike in) {
        return new Inp(BuiltInRegistries.ITEM.getKey(in.asItem()).getPath(), Ingredient.of(in),
                ItemPredicate.Builder.item().of(items, in).build());
    }

    public static Inp fromItems(HolderGetter<Item> items, String name, ItemLike... in) {
        return new Inp(name, Ingredient.of(in), ItemPredicate.Builder.item().of(items, in).build());
    }

    public static Inp fromTag(HolderGetter<Item> items, TagKey<Item> in) {
        return new Inp(in.location().getPath(), Ingredient.of(items.getOrThrow(in)),
                ItemPredicate.Builder.item().of(items, in).build());
    }

    *///?}
    private static ItemPredicate predicate(HolderLookup.Provider registries, ItemLike... in) {
        //? if >=1.21.2 {
        return ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), in).build();
        //?} else {
        /*return ItemPredicate.Builder.item().of(in).build();
        *///?}
    }
}
//?}
