//? if neoforge {
/*package nadiendev.constructionwand.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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

    private static void generateWandModel(ItemModelGenerator generator, String name) {
        // Sub-model for core_angel overlay
        generator.withExistingParent(name + "_core_angel", "item/handheld")
                .texture("layer0", generator.modLoc("item/" + name))
                .texture("layer1", generator.modLoc("item/overlay_core"));

        // Sub-model for core_destruction overlay
        generator.withExistingParent(name + "_core_destruction", "item/handheld")
                .texture("layer0", generator.modLoc("item/" + name))
                .texture("layer1", generator.modLoc("item/overlay_core"));

        // Sub-model for core_exchange overlay
        generator.withExistingParent(name + "_core_exchange", "item/handheld")
                .texture("layer0", generator.modLoc("item/" + name))
                .texture("layer1", generator.modLoc("item/overlay_core"));

        // Base model with overrides per core value
        generator.withExistingParent(name, "item/handheld")
                .texture("layer0", generator.modLoc("item/" + name))
                .override()
                    .predicate(generator.modLoc("wand_core"), 1.0f) // core_angel
                    .model(generator.getExistingFile(generator.modLoc("item/" + name + "_core_angel")))
                .end()
                .override()
                    .predicate(generator.modLoc("wand_core"), 2.0f) // core_destruction
                    .model(generator.getExistingFile(generator.modLoc("item/" + name + "_core_destruction")))
                .end()
                .override()
                    .predicate(generator.modLoc("wand_core"), 3.0f) // core_exchange
                    .model(generator.getExistingFile(generator.modLoc("item/" + name + "_core_exchange")))
                .end();
    }
}
*///?}
