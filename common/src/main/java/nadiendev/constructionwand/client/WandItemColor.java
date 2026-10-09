package nadiendev.constructionwand.client;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import nadiendev.constructionwand.basics.option.WandOptions;
import nadiendev.constructionwand.items.wand.ItemWand;

/** Tints the core overlay layer of the wand models with the colour of the installed core. */
public class WandItemColor implements ItemColor
{
    @Override
    public int getColor(ItemStack stack, int layer) {
        return (layer == 1 && stack.getItem() instanceof ItemWand) ? new WandOptions(stack).cores.get().getColor() : -1;
    }
}
