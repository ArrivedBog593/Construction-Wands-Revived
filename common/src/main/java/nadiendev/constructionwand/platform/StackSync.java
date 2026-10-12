package nadiendev.constructionwand.platform;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * The loader transfer APIs modify the stack they are handed by swapping it for a copy inside a throwaway container.
 * Container items (backpacks and the like) are therefore worked on as a copy, and this writes the result back into the
 * stack that actually sits in the player's inventory.
 */
public final class StackSync
{
    private StackSync() {}

    /** Makes {@code target} (count and components) match {@code source}. The item is assumed to be the same. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void copyInto(ItemStack target, ItemStack source) {
        if(target == source) return;

        Set<DataComponentType<?>> types = new HashSet<>();
        types.addAll(target.getComponents().keySet());
        types.addAll(source.getComponents().keySet());

        for(DataComponentType type : types) {
            Object value = source.get(type);
            if(value == null) target.remove(type);
            else if(!value.equals(target.get(type))) target.set(type, value);
        }
        target.setCount(source.getCount());
    }
}
