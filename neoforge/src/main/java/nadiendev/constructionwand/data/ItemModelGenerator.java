package nadiendev.constructionwand.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import nadiendev.constructionwand.registry.RegistryObject;
import nadiendev.constructionwand.items.wand.ItemWand;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.items.ModItems;

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
                generateWandModel(this, name);
            else if(item instanceof BlockItem)
                withExistingParent(name, modLoc("block/" + name));
            else withExistingParent(name, "item/generated").texture("layer0", "item/" + name);
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return ConstructionWand.MODNAME + " item models";
    }

    private static final String[] CORES = {"core_angel", "core_destruction", "core_exchange"};
    private static final String[] CORE_OVERLAYS = {"overlay_angel", "overlay_destruction", "overlay_exchange"};

    private static void generateWandModel(ItemModelGenerator generator, String name) {
        // Sub-models: the wand with the overlay of its core
        for(int i = 0; i < CORES.length; i++) {
            generator.withExistingParent(name + "_" + CORES[i], "item/handheld")
                    .texture("layer0", generator.modLoc("item/" + name))
                    .texture("layer1", generator.modLoc("item/" + CORE_OVERLAYS[i]));
        }
        // Base model with overrides per wand_core value: 1-3 the core
        ItemModelBuilder base = generator.withExistingParent(name, "item/handheld")
                .texture("layer0", generator.modLoc("item/" + name));
        for(int i = 0; i < CORES.length; i++) {
            base.override()
                    .predicate(generator.modLoc("wand_core"), i + 1.0f)
                    .model(generator.getExistingFile(generator.modLoc("item/" + name + "_" + CORES[i])))
                    .end();
        }
    }
}
