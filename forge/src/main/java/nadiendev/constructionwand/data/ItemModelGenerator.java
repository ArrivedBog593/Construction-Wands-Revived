package nadiendev.constructionwand.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.items.ModItems;
import nadiendev.constructionwand.items.wand.ItemWand;
import nadiendev.constructionwand.registry.RegistryObject;

import javax.annotation.Nonnull;

public class ItemModelGenerator extends ItemModelProvider
{
    public ItemModelGenerator(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, ConstructionWand.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for(RegistryObject<? extends Item> itemObject : ModItems.ITEMS.entries()) {
            Item item = itemObject.get();
            String name = itemObject.getId().getPath();

            if(item instanceof ItemWand)
                generateWandModel(name);
            else if(item instanceof BlockItem)
                withExistingParent(name, modLoc("block/" + name));
            else withExistingParent(name, "item/generated").texture("layer0", "item/" + name);
        }
    }

    private static final String[] CORES = {"core_angel", "core_destruction", "core_exchange"};
    private static final String[] CORE_OVERLAYS = {"overlay_angel", "overlay_destruction", "overlay_exchange"};

    private void generateWandModel(String name) {
        // Sub-models: the wand with the overlay of its core
        ItemModelBuilder[] withCore = new ItemModelBuilder[CORES.length];
        for(int i = 0; i < CORES.length; i++) {
            withCore[i] = withExistingParent(name + "_" + CORES[i], "item/handheld")
                    .texture("layer0", modLoc("item/" + name))
                    .texture("layer1", modLoc("item/" + CORE_OVERLAYS[i]));
        }
        // Base model with overrides per wand_core value: 1-3 the core
        ItemModelBuilder base = withExistingParent(name, "item/handheld")
                .texture("layer0", modLoc("item/" + name));
        for(int i = 0; i < CORES.length; i++) {
            base.override()
                    .predicate(modLoc("wand_core"), i + 1.0f)
                    .model(withCore[i])
                    .end();
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return ConstructionWand.MODNAME + " item models";
    }
}
